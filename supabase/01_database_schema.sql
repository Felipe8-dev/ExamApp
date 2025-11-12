-- =====================================================
-- ESQUEMA DE BASE DE DATOS PARA EXAMAPP
-- Sistema de Gestión de Exámenes Educativos
-- =====================================================

-- Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =====================================================
-- TABLA: profiles (extiende auth.users de Supabase)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    email TEXT UNIQUE NOT NULL,
    full_name TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('profesor', 'estudiante')),
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Índices para mejorar rendimiento
CREATE INDEX IF NOT EXISTS idx_profiles_role ON public.profiles(role);
CREATE INDEX IF NOT EXISTS idx_profiles_email ON public.profiles(email);

-- =====================================================
-- TABLA: exams (Exámenes creados por profesores)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.exams (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    description TEXT,
    professor_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    subject TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL CHECK (duration_minutes > 0),
    passing_score DECIMAL(5,2) NOT NULL DEFAULT 60.00 CHECK (passing_score >= 0 AND passing_score <= 100),
    
    -- Control de acceso
    is_public BOOLEAN DEFAULT FALSE,
    access_code TEXT UNIQUE, -- Código para unirse al examen
    
    -- Configuración del examen
    shuffle_questions BOOLEAN DEFAULT FALSE,
    show_results_immediately BOOLEAN DEFAULT TRUE,
    allow_review BOOLEAN DEFAULT TRUE,
    max_attempts INTEGER DEFAULT 1 CHECK (max_attempts > 0),
    
    -- Fechas de disponibilidad
    available_from TIMESTAMP WITH TIME ZONE,
    available_until TIMESTAMP WITH TIME ZONE,
    
    -- Metadatos
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    -- Validación de fechas
    CONSTRAINT valid_date_range CHECK (
        available_from IS NULL OR 
        available_until IS NULL OR 
        available_from < available_until
    )
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_exams_professor ON public.exams(professor_id);
CREATE INDEX IF NOT EXISTS idx_exams_access_code ON public.exams(access_code) WHERE access_code IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_exams_active ON public.exams(is_active) WHERE is_active = TRUE;

-- =====================================================
-- TABLA: questions (Preguntas de los exámenes)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.questions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_id UUID NOT NULL REFERENCES public.exams(id) ON DELETE CASCADE,
    question_text TEXT NOT NULL,
    question_type TEXT NOT NULL CHECK (question_type IN ('multiple_choice', 'true_false', 'open_ended')),
    points DECIMAL(5,2) NOT NULL DEFAULT 1.00 CHECK (points > 0),
    order_index INTEGER NOT NULL,
    explanation TEXT, -- Explicación de la respuesta correcta
    difficulty TEXT CHECK (difficulty IN ('facil', 'medio', 'dificil')),
    tags TEXT[], -- Array de tags para categorización
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(exam_id, order_index)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_questions_exam ON public.questions(exam_id);
CREATE INDEX IF NOT EXISTS idx_questions_type ON public.questions(question_type);

-- =====================================================
-- TABLA: question_options (Opciones para preguntas de opción múltiple)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.question_options (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    question_id UUID NOT NULL REFERENCES public.questions(id) ON DELETE CASCADE,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    order_index INTEGER NOT NULL,
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(question_id, order_index)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_question_options_question ON public.question_options(question_id);

-- =====================================================
-- TABLA: exam_attempts (Intentos de estudiantes en exámenes)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.exam_attempts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_id UUID NOT NULL REFERENCES public.exams(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    
    -- Estado del intento
    status TEXT NOT NULL DEFAULT 'in_progress' CHECK (status IN ('in_progress', 'completed', 'abandoned', 'timed_out')),
    
    -- Puntuación
    score DECIMAL(5,2) CHECK (score >= 0 AND score <= 100),
    total_points_earned DECIMAL(8,2),
    total_points_possible DECIMAL(8,2),
    passed BOOLEAN,
    
    -- Tiempos
    started_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    completed_at TIMESTAMP WITH TIME ZONE,
    time_spent_seconds INTEGER,
    
    -- Número de intento
    attempt_number INTEGER NOT NULL DEFAULT 1,
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(exam_id, student_id, attempt_number)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_exam_attempts_exam ON public.exam_attempts(exam_id);
CREATE INDEX IF NOT EXISTS idx_exam_attempts_student ON public.exam_attempts(student_id);
CREATE INDEX IF NOT EXISTS idx_exam_attempts_status ON public.exam_attempts(status);

-- =====================================================
-- TABLA: question_answers (Respuestas de estudiantes a preguntas)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.question_answers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    attempt_id UUID NOT NULL REFERENCES public.exam_attempts(id) ON DELETE CASCADE,
    question_id UUID NOT NULL REFERENCES public.questions(id) ON DELETE CASCADE,
    selected_option_id UUID REFERENCES public.question_options(id) ON DELETE SET NULL,
    
    -- Para preguntas abiertas
    answer_text TEXT,
    
    -- Calificación
    is_correct BOOLEAN,
    points_earned DECIMAL(5,2) DEFAULT 0,
    
    -- Tiempo de respuesta
    answered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    time_spent_seconds INTEGER,
    
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(attempt_id, question_id)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_question_answers_attempt ON public.question_answers(attempt_id);
CREATE INDEX IF NOT EXISTS idx_question_answers_question ON public.question_answers(question_id);

-- =====================================================
-- TABLA: exam_enrollments (Inscripciones a exámenes)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.exam_enrollments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    exam_id UUID NOT NULL REFERENCES public.exams(id) ON DELETE CASCADE,
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    
    enrolled_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    
    UNIQUE(exam_id, student_id)
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_exam_enrollments_exam ON public.exam_enrollments(exam_id);
CREATE INDEX IF NOT EXISTS idx_exam_enrollments_student ON public.exam_enrollments(student_id);

-- =====================================================
-- TABLA: ai_recommendations (Recomendaciones generadas por IA)
-- =====================================================
CREATE TABLE IF NOT EXISTS public.ai_recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    student_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    exam_id UUID REFERENCES public.exams(id) ON DELETE CASCADE,
    
    recommendation_type TEXT NOT NULL CHECK (recommendation_type IN ('study_focus', 'practice_area', 'weakness_identified', 'strength_identified')),
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    priority TEXT CHECK (priority IN ('baja', 'media', 'alta')),
    
    -- Métricas asociadas
    based_on_attempts INTEGER,
    average_score DECIMAL(5,2),
    
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    expires_at TIMESTAMP WITH TIME ZONE
);

-- Índices
CREATE INDEX IF NOT EXISTS idx_ai_recommendations_student ON public.ai_recommendations(student_id);
CREATE INDEX IF NOT EXISTS idx_ai_recommendations_unread ON public.ai_recommendations(is_read) WHERE is_read = FALSE;

-- =====================================================
-- FUNCIONES Y TRIGGERS
-- =====================================================

-- Función para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers para updated_at
CREATE TRIGGER update_profiles_updated_at BEFORE UPDATE ON public.profiles
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_exams_updated_at BEFORE UPDATE ON public.exams
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_questions_updated_at BEFORE UPDATE ON public.questions
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_exam_attempts_updated_at BEFORE UPDATE ON public.exam_attempts
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Función para crear perfil automáticamente cuando se registra un usuario
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (id, email, full_name, role)
    VALUES (
        NEW.id,
        NEW.email,
        COALESCE(NEW.raw_user_meta_data->>'full_name', 'Usuario'),
        COALESCE(NEW.raw_user_meta_data->>'role', 'estudiante')
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Trigger para crear perfil automáticamente
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- Función para calcular puntuación de un intento
CREATE OR REPLACE FUNCTION calculate_exam_attempt_score(attempt_id_param UUID)
RETURNS VOID AS $$
DECLARE
    total_earned DECIMAL(8,2);
    total_possible DECIMAL(8,2);
    percentage DECIMAL(5,2);
    passing_threshold DECIMAL(5,2);
    has_passed BOOLEAN;
BEGIN
    -- Calcular puntos ganados
    SELECT COALESCE(SUM(points_earned), 0)
    INTO total_earned
    FROM public.question_answers
    WHERE attempt_id = attempt_id_param;
    
    -- Calcular puntos posibles
    SELECT COALESCE(SUM(q.points), 0)
    INTO total_possible
    FROM public.questions q
    INNER JOIN public.exam_attempts ea ON ea.exam_id = q.exam_id
    WHERE ea.id = attempt_id_param;
    
    -- Calcular porcentaje
    IF total_possible > 0 THEN
        percentage := (total_earned / total_possible) * 100;
    ELSE
        percentage := 0;
    END IF;
    
    -- Obtener umbral de aprobación
    SELECT e.passing_score
    INTO passing_threshold
    FROM public.exams e
    INNER JOIN public.exam_attempts ea ON ea.exam_id = e.id
    WHERE ea.id = attempt_id_param;
    
    has_passed := percentage >= passing_threshold;
    
    -- Actualizar el intento
    UPDATE public.exam_attempts
    SET 
        total_points_earned = total_earned,
        total_points_possible = total_possible,
        score = percentage,
        passed = has_passed,
        updated_at = NOW()
    WHERE id = attempt_id_param;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- COMENTARIOS PARA DOCUMENTACIÓN
-- =====================================================

COMMENT ON TABLE public.profiles IS 'Perfiles de usuarios (profesores y estudiantes)';
COMMENT ON TABLE public.exams IS 'Exámenes creados por profesores';
COMMENT ON TABLE public.questions IS 'Preguntas que pertenecen a exámenes';
COMMENT ON TABLE public.question_options IS 'Opciones de respuesta para preguntas de opción múltiple';
COMMENT ON TABLE public.exam_attempts IS 'Intentos de estudiantes en exámenes';
COMMENT ON TABLE public.question_answers IS 'Respuestas de estudiantes a preguntas específicas';
COMMENT ON TABLE public.exam_enrollments IS 'Inscripciones de estudiantes a exámenes';
COMMENT ON TABLE public.ai_recommendations IS 'Recomendaciones generadas por IA para estudiantes';

