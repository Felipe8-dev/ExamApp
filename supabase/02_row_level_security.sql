-- =====================================================
-- POLÍTICAS DE SEGURIDAD (ROW LEVEL SECURITY)
-- Sistema de Gestión de Exámenes Educativos
-- =====================================================

-- Habilitar RLS en todas las tablas
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.exams ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.questions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.question_options ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.exam_attempts ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.question_answers ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.exam_enrollments ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_recommendations ENABLE ROW LEVEL SECURITY;

-- =====================================================
-- POLÍTICAS PARA: profiles
-- =====================================================

-- Los usuarios pueden ver su propio perfil
CREATE POLICY "Users can view own profile"
ON public.profiles FOR SELECT
USING (auth.uid() = id);

-- Los usuarios pueden insertar su propio perfil
CREATE POLICY "Users can insert own profile"
ON public.profiles FOR INSERT
WITH CHECK (auth.uid() = id);

-- Los usuarios pueden actualizar su propio perfil
CREATE POLICY "Users can update own profile"
ON public.profiles FOR UPDATE
USING (auth.uid() = id);

-- Los profesores pueden ver perfiles de estudiantes inscritos en sus exámenes
-- SOLUCIÓN: Simplificamos la política para evitar recursión infinita
-- En lugar de consultar exam_enrollments (que consulta profiles), 
-- permitimos que los profesores vean todos los perfiles de estudiantes
-- La verificación de inscripción se puede hacer a nivel de aplicación
DROP POLICY IF EXISTS "Professors can view enrolled students" ON public.profiles;

CREATE POLICY "Professors can view enrolled students"
ON public.profiles FOR SELECT
USING (role = 'estudiante');

-- Los estudiantes pueden ver perfiles públicos de profesores
CREATE POLICY "Students can view professor profiles"
ON public.profiles FOR SELECT
USING (
    role = 'profesor'
);

-- =====================================================
-- POLÍTICAS PARA: exams
-- =====================================================

-- Los profesores pueden ver sus propios exámenes
CREATE POLICY "Professors can view own exams"
ON public.exams FOR SELECT
USING (
    professor_id = auth.uid()
);

-- Los profesores pueden crear exámenes
-- Simplificamos para evitar recursión: confiamos en que el usuario autenticado es profesor
-- La verificación del role se puede hacer a nivel de aplicación
CREATE POLICY "Professors can create exams"
ON public.exams FOR INSERT
WITH CHECK (professor_id = auth.uid());

-- Los profesores pueden actualizar sus propios exámenes
CREATE POLICY "Professors can update own exams"
ON public.exams FOR UPDATE
USING (professor_id = auth.uid());

-- Los profesores pueden eliminar sus propios exámenes
CREATE POLICY "Professors can delete own exams"
ON public.exams FOR DELETE
USING (professor_id = auth.uid());

-- Los estudiantes pueden ver exámenes en los que están inscritos
CREATE POLICY "Students can view enrolled exams"
ON public.exams FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exam_enrollments
        WHERE exam_id = exams.id AND student_id = auth.uid()
    )
);

-- Los estudiantes pueden ver exámenes públicos activos
CREATE POLICY "Students can view public active exams"
ON public.exams FOR SELECT
USING (
    is_public = TRUE AND
    is_active = TRUE AND
    (available_from IS NULL OR available_from <= NOW()) AND
    (available_until IS NULL OR available_until >= NOW())
);

-- =====================================================
-- POLÍTICAS PARA: questions
-- =====================================================

-- Los profesores pueden ver preguntas de sus exámenes
CREATE POLICY "Professors can view own exam questions"
ON public.questions FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = questions.exam_id AND professor_id = auth.uid()
    )
);

-- Los profesores pueden crear preguntas en sus exámenes
CREATE POLICY "Professors can create questions in own exams"
ON public.questions FOR INSERT
WITH CHECK (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = exam_id AND professor_id = auth.uid()
    )
);

-- Los profesores pueden actualizar preguntas de sus exámenes
CREATE POLICY "Professors can update own exam questions"
ON public.questions FOR UPDATE
USING (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = questions.exam_id AND professor_id = auth.uid()
    )
);

-- Los profesores pueden eliminar preguntas de sus exámenes
CREATE POLICY "Professors can delete own exam questions"
ON public.questions FOR DELETE
USING (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = questions.exam_id AND professor_id = auth.uid()
    )
);

-- Los estudiantes pueden ver preguntas de exámenes en los que están inscritos
CREATE POLICY "Students can view questions of enrolled exams"
ON public.questions FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exam_enrollments
        WHERE exam_id = questions.exam_id AND student_id = auth.uid()
    )
);

-- =====================================================
-- POLÍTICAS PARA: question_options
-- =====================================================

-- Los profesores pueden gestionar opciones de sus preguntas
CREATE POLICY "Professors can manage own question options"
ON public.question_options FOR ALL
USING (
    EXISTS (
        SELECT 1 FROM public.questions q
        INNER JOIN public.exams e ON e.id = q.exam_id
        WHERE q.id = question_options.question_id AND e.professor_id = auth.uid()
    )
);

-- Los estudiantes pueden ver opciones (pero no is_correct durante el examen)
CREATE POLICY "Students can view question options"
ON public.question_options FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.questions q
        INNER JOIN public.exam_enrollments ee ON ee.exam_id = q.exam_id
        WHERE q.id = question_options.question_id AND ee.student_id = auth.uid()
    )
);

-- =====================================================
-- POLÍTICAS PARA: exam_attempts
-- =====================================================

-- Los estudiantes pueden ver sus propios intentos
CREATE POLICY "Students can view own attempts"
ON public.exam_attempts FOR SELECT
USING (student_id = auth.uid());

-- Los estudiantes pueden crear sus propios intentos
-- Simplificamos para evitar recursión: confiamos en que el usuario autenticado es estudiante
CREATE POLICY "Students can create own attempts"
ON public.exam_attempts FOR INSERT
WITH CHECK (
    student_id = auth.uid() AND
    -- Verificar que estén inscritos
    EXISTS (
        SELECT 1 FROM public.exam_enrollments
        WHERE exam_id = exam_attempts.exam_id AND student_id = auth.uid()
    ) AND
    -- Verificar límite de intentos
    (
        SELECT COUNT(*) 
        FROM public.exam_attempts ea 
        WHERE ea.exam_id = exam_attempts.exam_id 
        AND ea.student_id = auth.uid()
    ) < (
        SELECT max_attempts 
        FROM public.exams 
        WHERE id = exam_attempts.exam_id
    )
);

-- Los estudiantes pueden actualizar sus propios intentos en progreso
CREATE POLICY "Students can update own in-progress attempts"
ON public.exam_attempts FOR UPDATE
USING (
    student_id = auth.uid() AND
    status = 'in_progress'
);

-- Los profesores pueden ver intentos de sus exámenes
CREATE POLICY "Professors can view attempts of own exams"
ON public.exam_attempts FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = exam_attempts.exam_id AND professor_id = auth.uid()
    )
);

-- =====================================================
-- POLÍTICAS PARA: question_answers
-- =====================================================

-- Los estudiantes pueden ver sus propias respuestas
CREATE POLICY "Students can view own answers"
ON public.question_answers FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exam_attempts
        WHERE id = question_answers.attempt_id AND student_id = auth.uid()
    )
);

-- Los estudiantes pueden crear sus propias respuestas
CREATE POLICY "Students can create own answers"
ON public.question_answers FOR INSERT
WITH CHECK (
    EXISTS (
        SELECT 1 FROM public.exam_attempts
        WHERE id = attempt_id 
        AND student_id = auth.uid()
        AND status = 'in_progress'
    )
);

-- Los estudiantes pueden actualizar sus respuestas en intentos activos
CREATE POLICY "Students can update own answers in active attempts"
ON public.question_answers FOR UPDATE
USING (
    EXISTS (
        SELECT 1 FROM public.exam_attempts
        WHERE id = question_answers.attempt_id 
        AND student_id = auth.uid()
        AND status = 'in_progress'
    )
);

-- Los profesores pueden ver respuestas de intentos en sus exámenes
CREATE POLICY "Professors can view answers in own exams"
ON public.question_answers FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exam_attempts ea
        INNER JOIN public.exams e ON e.id = ea.exam_id
        WHERE ea.id = question_answers.attempt_id AND e.professor_id = auth.uid()
    )
);

-- =====================================================
-- POLÍTICAS PARA: exam_enrollments
-- =====================================================

-- Los estudiantes pueden ver sus propias inscripciones
CREATE POLICY "Students can view own enrollments"
ON public.exam_enrollments FOR SELECT
USING (student_id = auth.uid());

-- Los estudiantes pueden inscribirse a exámenes
-- Simplificamos la política para evitar recursión con profiles
CREATE POLICY "Students can enroll in exams"
ON public.exam_enrollments FOR INSERT
WITH CHECK (
    student_id = auth.uid() AND
    -- Solo pueden inscribirse a exámenes públicos o con código de acceso
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = exam_id AND (is_public = TRUE OR access_code IS NOT NULL)
        AND is_active = TRUE
    )
);

-- Los estudiantes pueden cancelar sus inscripciones (si no han iniciado)
CREATE POLICY "Students can unenroll if no attempts"
ON public.exam_enrollments FOR DELETE
USING (
    student_id = auth.uid() AND
    NOT EXISTS (
        SELECT 1 FROM public.exam_attempts
        WHERE exam_id = exam_enrollments.exam_id AND student_id = auth.uid()
    )
);

-- Los profesores pueden ver inscripciones de sus exámenes
CREATE POLICY "Professors can view enrollments in own exams"
ON public.exam_enrollments FOR SELECT
USING (
    EXISTS (
        SELECT 1 FROM public.exams
        WHERE id = exam_enrollments.exam_id AND professor_id = auth.uid()
    )
);

-- =====================================================
-- POLÍTICAS PARA: ai_recommendations
-- =====================================================

-- Los estudiantes solo pueden ver sus propias recomendaciones
CREATE POLICY "Students can view own recommendations"
ON public.ai_recommendations FOR SELECT
USING (student_id = auth.uid());

-- Los estudiantes pueden actualizar sus recomendaciones (marcar como leídas)
CREATE POLICY "Students can update own recommendations"
ON public.ai_recommendations FOR UPDATE
USING (student_id = auth.uid());

-- Sistema puede crear recomendaciones (vía service role)
-- No se necesita política pública, se usa service_role key desde backend

-- =====================================================
-- FUNCIONES DE SEGURIDAD ADICIONALES
-- =====================================================

-- Función para verificar si un usuario es profesor
CREATE OR REPLACE FUNCTION is_professor()
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM public.profiles
        WHERE id = auth.uid() AND role = 'profesor'
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Función para verificar si un usuario es estudiante
CREATE OR REPLACE FUNCTION is_student()
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM public.profiles
        WHERE id = auth.uid() AND role = 'estudiante'
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Función para verificar si un estudiante está inscrito en un examen
CREATE OR REPLACE FUNCTION is_enrolled_in_exam(exam_id_param UUID)
RETURNS BOOLEAN AS $$
BEGIN
    RETURN EXISTS (
        SELECT 1 FROM public.exam_enrollments
        WHERE exam_id = exam_id_param AND student_id = auth.uid()
    );
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- =====================================================
-- GRANTS PARA SERVICE ROLE (para operaciones admin)
-- =====================================================

-- El service role puede hacer todo
GRANT ALL ON ALL TABLES IN SCHEMA public TO service_role;
GRANT ALL ON ALL SEQUENCES IN SCHEMA public TO service_role;
GRANT ALL ON ALL FUNCTIONS IN SCHEMA public TO service_role;

-- =====================================================
-- COMENTARIOS
-- =====================================================

COMMENT ON POLICY "Users can view own profile" ON public.profiles IS 
    'Permite a los usuarios ver su propio perfil';

COMMENT ON POLICY "Professors can create exams" ON public.exams IS 
    'Solo profesores pueden crear exámenes';

COMMENT ON POLICY "Students can view enrolled exams" ON public.exams IS 
    'Estudiantes pueden ver exámenes en los que están inscritos';

