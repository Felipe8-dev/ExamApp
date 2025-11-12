-- =====================================================
-- DATOS INICIALES DE EJEMPLO
-- Sistema de Gestión de Exámenes Educativos
-- =====================================================

-- NOTA: Estos son datos de ejemplo para desarrollo
-- NO ejecutar en producción

-- =====================================================
-- LIMPIEZA (solo para desarrollo)
-- =====================================================
-- UNCOMMENT PARA LIMPIAR DATOS
-- TRUNCATE TABLE public.ai_recommendations CASCADE;
-- TRUNCATE TABLE public.question_answers CASCADE;
-- TRUNCATE TABLE public.exam_attempts CASCADE;
-- TRUNCATE TABLE public.exam_enrollments CASCADE;
-- TRUNCATE TABLE public.question_options CASCADE;
-- TRUNCATE TABLE public.questions CASCADE;
-- TRUNCATE TABLE public.exams CASCADE;
-- TRUNCATE TABLE public.profiles CASCADE;

-- =====================================================
-- USUARIOS DE EJEMPLO
-- =====================================================
-- Para crear usuarios de ejemplo, debes usar la API de Supabase Auth
-- o crear usuarios desde el Dashboard de Supabase

-- Ejemplo de cómo se verían los perfiles después de registrarse:
/*
-- Profesor de ejemplo
INSERT INTO public.profiles (id, email, full_name, role, avatar_url)
VALUES 
    ('11111111-1111-1111-1111-111111111111', 'profesor@ejemplo.com', 'Dr. Juan Pérez', 'profesor', NULL),
    ('22222222-2222-2222-2222-222222222222', 'maria.profesor@ejemplo.com', 'Dra. María García', 'profesor', NULL);

-- Estudiantes de ejemplo
INSERT INTO public.profiles (id, email, full_name, role, avatar_url)
VALUES 
    ('33333333-3333-3333-3333-333333333333', 'estudiante1@ejemplo.com', 'Carlos López', 'estudiante', NULL),
    ('44444444-4444-4444-4444-444444444444', 'estudiante2@ejemplo.com', 'Ana Martínez', 'estudiante', NULL),
    ('55555555-5555-5555-5555-555555555555', 'estudiante3@ejemplo.com', 'Pedro Sánchez', 'estudiante', NULL);
*/

-- =====================================================
-- EXAMEN DE EJEMPLO
-- =====================================================
-- Nota: Reemplaza los IDs con los IDs reales de tu base de datos

/*
-- Examen 1: Matemáticas Básicas
INSERT INTO public.exams (id, title, description, professor_id, subject, duration_minutes, passing_score, is_public, access_code, shuffle_questions, show_results_immediately, allow_review, max_attempts)
VALUES (
    '10000000-0000-0000-0000-000000000001',
    'Matemáticas Básicas - Álgebra',
    'Examen de álgebra básica para estudiantes de primer año',
    '11111111-1111-1111-1111-111111111111', -- ID del profesor
    'Matemáticas',
    60, -- 60 minutos
    60.00, -- 60% para aprobar
    TRUE, -- público
    'MAT2024', -- código de acceso
    TRUE, -- mezclar preguntas
    TRUE, -- mostrar resultados inmediatamente
    TRUE, -- permitir revisión
    3 -- máximo 3 intentos
);

-- Preguntas para Examen 1
INSERT INTO public.questions (id, exam_id, question_text, question_type, points, order_index, explanation, difficulty)
VALUES 
    ('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '¿Cuál es el resultado de 2x + 3 = 11?', 'multiple_choice', 1.00, 1, 'Restamos 3 de ambos lados y dividimos por 2: x = 4', 'facil'),
    ('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', '¿Es verdadero que (a + b)² = a² + b²?', 'true_false', 1.00, 2, 'Falso. La fórmula correcta es (a + b)² = a² + 2ab + b²', 'medio'),
    ('20000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Simplifica: 3x + 5x - 2x', 'multiple_choice', 1.00, 3, 'Sumamos los coeficientes: 3 + 5 - 2 = 6, resultado: 6x', 'facil');

-- Opciones para pregunta 1
INSERT INTO public.question_options (question_id, option_text, is_correct, order_index)
VALUES 
    ('20000000-0000-0000-0000-000000000001', 'x = 4', TRUE, 1),
    ('20000000-0000-0000-0000-000000000001', 'x = 7', FALSE, 2),
    ('20000000-0000-0000-0000-000000000001', 'x = 8', FALSE, 3),
    ('20000000-0000-0000-0000-000000000001', 'x = 14', FALSE, 4);

-- Opciones para pregunta 2 (true/false)
INSERT INTO public.question_options (question_id, option_text, is_correct, order_index)
VALUES 
    ('20000000-0000-0000-0000-000000000002', 'Verdadero', FALSE, 1),
    ('20000000-0000-0000-0000-000000000002', 'Falso', TRUE, 2);

-- Opciones para pregunta 3
INSERT INTO public.question_options (question_id, option_text, is_correct, order_index)
VALUES 
    ('20000000-0000-0000-0000-000000000003', '6x', TRUE, 1),
    ('20000000-0000-0000-0000-000000000003', '10x', FALSE, 2),
    ('20000000-0000-0000-0000-000000000003', '4x', FALSE, 3),
    ('20000000-0000-0000-0000-000000000003', '8x', FALSE, 4);

-- Examen 2: Historia Universal
INSERT INTO public.exams (id, title, description, professor_id, subject, duration_minutes, passing_score, is_public, access_code, max_attempts)
VALUES (
    '10000000-0000-0000-0000-000000000002',
    'Historia Universal - Edad Media',
    'Examen sobre eventos importantes de la Edad Media',
    '22222222-2222-2222-2222-222222222222', -- ID del profesor
    'Historia',
    45,
    70.00,
    FALSE,
    'HIST2024',
    2
);

-- Inscripciones de ejemplo
INSERT INTO public.exam_enrollments (exam_id, student_id)
VALUES 
    ('10000000-0000-0000-0000-000000000001', '33333333-3333-3333-3333-333333333333'),
    ('10000000-0000-0000-0000-000000000001', '44444444-4444-4444-4444-444444444444'),
    ('10000000-0000-0000-0000-000000000002', '33333333-3333-3333-3333-333333333333');
*/

-- =====================================================
-- VIEWS ÚTILES PARA CONSULTAS
-- =====================================================

-- Vista: Estadísticas de exámenes
CREATE OR REPLACE VIEW exam_statistics AS
SELECT 
    e.id AS exam_id,
    e.title,
    e.professor_id,
    COUNT(DISTINCT ee.student_id) AS total_enrolled,
    COUNT(DISTINCT ea.id) AS total_attempts,
    COUNT(DISTINCT ea.id) FILTER (WHERE ea.status = 'completed') AS completed_attempts,
    AVG(ea.score) FILTER (WHERE ea.status = 'completed') AS average_score,
    COUNT(DISTINCT ea.student_id) FILTER (WHERE ea.passed = TRUE) AS students_passed
FROM public.exams e
LEFT JOIN public.exam_enrollments ee ON ee.exam_id = e.id
LEFT JOIN public.exam_attempts ea ON ea.exam_id = e.id
GROUP BY e.id, e.title, e.professor_id;

-- Vista: Historial de estudiante
CREATE OR REPLACE VIEW student_exam_history AS
SELECT 
    ea.student_id,
    p.full_name AS student_name,
    e.id AS exam_id,
    e.title AS exam_title,
    e.subject,
    ea.id AS attempt_id,
    ea.attempt_number,
    ea.status,
    ea.score,
    ea.passed,
    ea.started_at,
    ea.completed_at,
    ea.time_spent_seconds
FROM public.exam_attempts ea
INNER JOIN public.exams e ON e.id = ea.exam_id
INNER JOIN public.profiles p ON p.id = ea.student_id
ORDER BY ea.started_at DESC;

-- Vista: Preguntas con sus opciones
CREATE OR REPLACE VIEW questions_with_options AS
SELECT 
    q.id AS question_id,
    q.exam_id,
    q.question_text,
    q.question_type,
    q.points,
    q.order_index,
    q.difficulty,
    jsonb_agg(
        jsonb_build_object(
            'id', qo.id,
            'option_text', qo.option_text,
            'is_correct', qo.is_correct,
            'order_index', qo.order_index
        ) ORDER BY qo.order_index
    ) AS options
FROM public.questions q
LEFT JOIN public.question_options qo ON qo.question_id = q.id
GROUP BY q.id, q.exam_id, q.question_text, q.question_type, q.points, q.order_index, q.difficulty
ORDER BY q.order_index;

-- =====================================================
-- FUNCIONES ÚTILES
-- =====================================================

-- Función para obtener el siguiente número de intento
CREATE OR REPLACE FUNCTION get_next_attempt_number(exam_id_param UUID, student_id_param UUID)
RETURNS INTEGER AS $$
DECLARE
    next_attempt INTEGER;
BEGIN
    SELECT COALESCE(MAX(attempt_number), 0) + 1
    INTO next_attempt
    FROM public.exam_attempts
    WHERE exam_id = exam_id_param AND student_id = student_id_param;
    
    RETURN next_attempt;
END;
$$ LANGUAGE plpgsql;

-- Función para verificar si un estudiante puede iniciar un nuevo intento
CREATE OR REPLACE FUNCTION can_start_new_attempt(exam_id_param UUID, student_id_param UUID)
RETURNS BOOLEAN AS $$
DECLARE
    current_attempts INTEGER;
    max_allowed INTEGER;
    has_active_attempt BOOLEAN;
BEGIN
    -- Verificar si hay un intento activo
    SELECT EXISTS(
        SELECT 1 FROM public.exam_attempts
        WHERE exam_id = exam_id_param 
        AND student_id = student_id_param
        AND status = 'in_progress'
    ) INTO has_active_attempt;
    
    IF has_active_attempt THEN
        RETURN FALSE;
    END IF;
    
    -- Contar intentos completados
    SELECT COUNT(*)
    INTO current_attempts
    FROM public.exam_attempts
    WHERE exam_id = exam_id_param AND student_id = student_id_param;
    
    -- Obtener máximo de intentos permitidos
    SELECT max_attempts
    INTO max_allowed
    FROM public.exams
    WHERE id = exam_id_param;
    
    RETURN current_attempts < max_allowed;
END;
$$ LANGUAGE plpgsql;

-- Función para inscribirse a un examen con código
CREATE OR REPLACE FUNCTION enroll_with_access_code(exam_id_param UUID, access_code_param TEXT)
RETURNS BOOLEAN AS $$
DECLARE
    valid_exam BOOLEAN;
BEGIN
    -- Verificar que el código sea correcto
    SELECT EXISTS(
        SELECT 1 FROM public.exams
        WHERE id = exam_id_param 
        AND access_code = access_code_param
        AND is_active = TRUE
    ) INTO valid_exam;
    
    IF NOT valid_exam THEN
        RETURN FALSE;
    END IF;
    
    -- Inscribir al estudiante
    INSERT INTO public.exam_enrollments (exam_id, student_id)
    VALUES (exam_id_param, auth.uid())
    ON CONFLICT (exam_id, student_id) DO NOTHING;
    
    RETURN TRUE;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- =====================================================
-- ÍNDICES ADICIONALES PARA RENDIMIENTO
-- =====================================================

CREATE INDEX IF NOT EXISTS idx_exam_attempts_completed 
ON public.exam_attempts(exam_id, student_id) 
WHERE status = 'completed';

CREATE INDEX IF NOT EXISTS idx_questions_exam_order 
ON public.questions(exam_id, order_index);

CREATE INDEX IF NOT EXISTS idx_ai_recommendations_student_unread 
ON public.ai_recommendations(student_id, created_at DESC) 
WHERE is_read = FALSE;

-- =====================================================
-- COMENTARIOS
-- =====================================================

COMMENT ON VIEW exam_statistics IS 'Estadísticas agregadas de cada examen';
COMMENT ON VIEW student_exam_history IS 'Historial completo de intentos de exámenes por estudiante';
COMMENT ON VIEW questions_with_options IS 'Preguntas con sus opciones en formato JSON';

