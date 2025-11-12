-- =====================================================
-- SCRIPT DE VERIFICACIÓN - EXAMAPP
-- Ejecuta este script para verificar que todo está correctamente configurado
-- =====================================================

-- =====================================================
-- PASO 1: Verificar que todas las tablas existen
-- =====================================================

SELECT 'PASO 1: Verificando tablas...' as paso;

SELECT 
    table_name,
    CASE 
        WHEN table_name IN ('profiles', 'exams', 'questions', 'question_options', 
                           'exam_attempts', 'question_answers', 'exam_enrollments', 
                           'ai_recommendations') 
        THEN '✅ OK'
        ELSE '❌ ERROR'
    END as estado
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_name IN ('profiles', 'exams', 'questions', 'question_options', 
                   'exam_attempts', 'question_answers', 'exam_enrollments', 
                   'ai_recommendations')
ORDER BY table_name;

-- Resultado esperado: 8 tablas con ✅ OK

-- =====================================================
-- PASO 2: Verificar columnas principales de cada tabla
-- =====================================================

SELECT 'PASO 2: Verificando columnas de profiles...' as paso;

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_schema = 'public' AND table_name = 'profiles'
ORDER BY ordinal_position;

-- Deberías ver: id, email, full_name, role, avatar_url, created_at, updated_at

-- =====================================================
-- PASO 3: Verificar que Row Level Security está habilitado
-- =====================================================

SELECT 'PASO 3: Verificando RLS...' as paso;

SELECT 
    tablename,
    rowsecurity as rls_habilitado,
    CASE 
        WHEN rowsecurity THEN '✅ RLS Habilitado'
        ELSE '❌ RLS Deshabilitado - EJECUTA 02_row_level_security.sql'
    END as estado
FROM pg_tables
WHERE schemaname = 'public'
AND tablename IN ('profiles', 'exams', 'questions', 'question_options', 
                  'exam_attempts', 'question_answers', 'exam_enrollments', 
                  'ai_recommendations')
ORDER BY tablename;

-- Resultado esperado: Todas las tablas con RLS Habilitado

-- =====================================================
-- PASO 4: Contar políticas de seguridad
-- =====================================================

SELECT 'PASO 4: Verificando políticas de seguridad...' as paso;

SELECT 
    tablename,
    COUNT(*) as numero_de_politicas,
    CASE 
        WHEN COUNT(*) > 0 THEN '✅ OK'
        ELSE '❌ Sin políticas - EJECUTA 02_row_level_security.sql'
    END as estado
FROM pg_policies
WHERE schemaname = 'public'
GROUP BY tablename
ORDER BY tablename;

-- Resultado esperado: Cada tabla debe tener al menos 2-5 políticas

-- =====================================================
-- PASO 5: Verificar triggers
-- =====================================================

SELECT 'PASO 5: Verificando triggers...' as paso;

SELECT 
    trigger_name,
    event_object_table as tabla,
    action_timing as cuando,
    event_manipulation as accion,
    CASE 
        WHEN trigger_name = 'on_auth_user_created' THEN '✅ Trigger de creación de perfil'
        WHEN trigger_name LIKE '%updated_at%' THEN '✅ Trigger de actualización'
        ELSE '✅ Otro trigger'
    END as descripcion
FROM information_schema.triggers
WHERE trigger_schema = 'public'
ORDER BY trigger_name;

-- Deberías ver el trigger 'on_auth_user_created' y varios triggers de updated_at

-- =====================================================
-- PASO 6: Verificar funciones importantes
-- =====================================================

SELECT 'PASO 6: Verificando funciones...' as paso;

SELECT 
    routine_name as nombre_funcion,
    CASE 
        WHEN routine_name = 'handle_new_user' THEN '✅ Crea perfiles automáticamente'
        WHEN routine_name = 'update_updated_at_column' THEN '✅ Actualiza timestamps'
        WHEN routine_name = 'calculate_exam_attempt_score' THEN '✅ Calcula puntuaciones'
        WHEN routine_name = 'can_start_new_attempt' THEN '✅ Valida intentos'
        WHEN routine_name = 'is_professor' THEN '✅ Verifica rol profesor'
        WHEN routine_name = 'is_student' THEN '✅ Verifica rol estudiante'
        ELSE '✅ Otra función'
    END as descripcion
FROM information_schema.routines
WHERE routine_schema = 'public'
AND routine_type = 'FUNCTION'
ORDER BY routine_name;

-- =====================================================
-- PASO 7: Verificar vistas
-- =====================================================

SELECT 'PASO 7: Verificando vistas...' as paso;

SELECT 
    table_name as nombre_vista,
    CASE 
        WHEN table_name = 'exam_statistics' THEN '✅ Estadísticas de exámenes'
        WHEN table_name = 'student_exam_history' THEN '✅ Historial de estudiantes'
        WHEN table_name = 'questions_with_options' THEN '✅ Preguntas con opciones'
        ELSE '✅ Otra vista'
    END as descripcion
FROM information_schema.views
WHERE table_schema = 'public'
ORDER BY table_name;

-- Deberías ver 3 vistas

-- =====================================================
-- PASO 8: Verificar índices
-- =====================================================

SELECT 'PASO 8: Verificando índices...' as paso;

SELECT 
    tablename,
    indexname,
    '✅ Índice creado' as estado
FROM pg_indexes
WHERE schemaname = 'public'
AND indexname LIKE 'idx_%'
ORDER BY tablename, indexname;

-- Deberías ver múltiples índices para optimizar las consultas

-- =====================================================
-- PASO 9: Probar creación de perfil (simulación)
-- =====================================================

SELECT 'PASO 9: Verificando que la función handle_new_user existe...' as paso;

SELECT 
    CASE 
        WHEN EXISTS (
            SELECT 1 FROM pg_proc 
            WHERE proname = 'handle_new_user'
        ) THEN '✅ Función handle_new_user encontrada - Los perfiles se crearán automáticamente'
        ELSE '❌ Función no encontrada - EJECUTA 01_database_schema.sql'
    END as resultado;

-- =====================================================
-- PASO 10: Verificar estructura completa
-- =====================================================

SELECT 'PASO 10: Resumen de verificación...' as paso;

SELECT 
    'Tablas' as componente,
    COUNT(*) as cantidad,
    CASE WHEN COUNT(*) >= 8 THEN '✅ OK' ELSE '❌ Faltan tablas' END as estado
FROM information_schema.tables
WHERE table_schema = 'public'
AND table_name IN ('profiles', 'exams', 'questions', 'question_options', 
                   'exam_attempts', 'question_answers', 'exam_enrollments', 
                   'ai_recommendations')

UNION ALL

SELECT 
    'Políticas RLS' as componente,
    COUNT(*) as cantidad,
    CASE WHEN COUNT(*) >= 20 THEN '✅ OK' ELSE '❌ Pocas políticas' END as estado
FROM pg_policies
WHERE schemaname = 'public'

UNION ALL

SELECT 
    'Funciones' as componente,
    COUNT(*) as cantidad,
    CASE WHEN COUNT(*) >= 5 THEN '✅ OK' ELSE '❌ Faltan funciones' END as estado
FROM information_schema.routines
WHERE routine_schema = 'public'
AND routine_type = 'FUNCTION'

UNION ALL

SELECT 
    'Vistas' as componente,
    COUNT(*) as cantidad,
    CASE WHEN COUNT(*) >= 3 THEN '✅ OK' ELSE '❌ Faltan vistas' END as estado
FROM information_schema.views
WHERE table_schema = 'public'

UNION ALL

SELECT 
    'Índices' as componente,
    COUNT(*) as cantidad,
    CASE WHEN COUNT(*) >= 10 THEN '✅ OK' ELSE '❌ Pocos índices' END as estado
FROM pg_indexes
WHERE schemaname = 'public'
AND indexname LIKE 'idx_%';

-- =====================================================
-- RESULTADO FINAL
-- =====================================================

SELECT '=====================================' as linea
UNION ALL SELECT '   VERIFICACIÓN COMPLETADA'
UNION ALL SELECT '======================================'
UNION ALL SELECT ''
UNION ALL SELECT 'Si todos los pasos muestran ✅ OK:'
UNION ALL SELECT '→ Tu base de datos está correctamente configurada'
UNION ALL SELECT '→ Puedes proceder a usar la aplicación'
UNION ALL SELECT ''
UNION ALL SELECT 'Si algún paso muestra ❌:'
UNION ALL SELECT '→ Ejecuta el script correspondiente'
UNION ALL SELECT '→ Vuelve a ejecutar este script de verificación'
UNION ALL SELECT '======================================'
UNION ALL SELECT '  ¡Listo para usar ExamApp! 🎉'
UNION ALL SELECT '======================================';

-- =====================================================
-- COMANDOS ÚTILES ADICIONALES
-- =====================================================

-- Para ver usuarios registrados (ejecutar DESPUÉS de registrar el primer usuario):
-- SELECT * FROM auth.users;

-- Para ver perfiles creados:
-- SELECT * FROM profiles;

-- Para ver políticas detalladas de una tabla:
-- SELECT * FROM pg_policies WHERE tablename = 'profiles';

-- Para ver todas las columnas de una tabla específica:
-- SELECT column_name, data_type, is_nullable 
-- FROM information_schema.columns 
-- WHERE table_name = 'profiles';

