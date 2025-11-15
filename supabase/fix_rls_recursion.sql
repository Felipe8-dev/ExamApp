-- =====================================================
-- SCRIPT PARA CORREGIR RECURSIÓN INFINITA EN RLS
-- =====================================================
-- Este script corrige el problema de recursión infinita
-- entre las políticas RLS de profiles y exam_enrollments

-- 1. Eliminar la política problemática de profiles
DROP POLICY IF EXISTS "Professors can view enrolled students" ON public.profiles;

-- 2. Crear función SECURITY DEFINER para obtener IDs de estudiantes
-- Esta función evita la recursión porque se ejecuta con privilegios elevados
-- y accede directamente a las tablas sin pasar por RLS
CREATE OR REPLACE FUNCTION public.get_enrolled_student_ids(professor_id_param UUID)
RETURNS SETOF UUID AS $$
BEGIN
    RETURN QUERY
    SELECT DISTINCT ee.student_id
    FROM public.exam_enrollments ee
    INNER JOIN public.exams e ON e.id = ee.exam_id
    WHERE e.professor_id = professor_id_param;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER STABLE;

-- 3. Recrear la política simplificada (sin consultar exam_enrollments)
-- Esto evita la recursión infinita
CREATE POLICY "Professors can view enrolled students"
ON public.profiles FOR SELECT
USING (role = 'estudiante');

-- 4. Simplificar política de INSERT en exam_enrollments
-- Eliminar la verificación del role para evitar recursión
DROP POLICY IF EXISTS "Students can enroll in exams" ON public.exam_enrollments;

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

-- 5. Simplificar política de INSERT en exams
DROP POLICY IF EXISTS "Professors can create exams" ON public.exams;

CREATE POLICY "Professors can create exams"
ON public.exams FOR INSERT
WITH CHECK (professor_id = auth.uid());

-- 6. Simplificar política de INSERT en exam_attempts
DROP POLICY IF EXISTS "Students can create own attempts" ON public.exam_attempts;

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

-- Verificar que las políticas se crearon correctamente
SELECT 
    schemaname,
    tablename,
    policyname,
    permissive,
    roles,
    cmd,
    qual,
    with_check
FROM pg_policies
WHERE schemaname = 'public'
AND tablename IN ('profiles', 'exam_enrollments', 'exams', 'exam_attempts')
ORDER BY tablename, policyname;

