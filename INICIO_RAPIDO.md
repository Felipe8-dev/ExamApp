# ⚡ Inicio Rápido - ExamApp con Supabase

Esta guía te permite empezar rápidamente. Para detalles completos, consulta `CONFIGURACION_SUPABASE.md`.

## 🚀 Configuración en 10 minutos

### 1. Crear proyecto en Supabase (2 min)

1. Ve a [supabase.com](https://supabase.com) y crea una cuenta
2. Crea un nuevo proyecto
3. Guarda tu **Project URL** y **API Key** (Settings > API)

### 2. Configurar la base de datos (3 min)

En Supabase, ve a **SQL Editor** y ejecuta en orden:

1. `supabase/01_database_schema.sql` - Crea las tablas
2. `supabase/02_row_level_security.sql` - Configura seguridad
3. `supabase/03_initial_data.sql` - Crea vistas y funciones

### 3. Configurar autenticación (2 min)

1. Ve a **Authentication** > **URL Configuration**
2. En **Site URL** pon: `examapp://auth-callback`
3. En **Redirect URLs** añade: `examapp://auth-callback`
4. (Opcional) Desactiva confirmación de email en **Providers** > **Email**

### 4. Actualizar credenciales en Android (1 min)

En `app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt`:

```kotlin
private const val SUPABASE_URL = "https://TU_PROJECT_REF.supabase.co"
private const val SUPABASE_ANON_KEY = "tu_anon_key_aqui"
```

### 5. Sincronizar y probar (2 min)

1. Sync Gradle en Android Studio
2. Ejecuta la app
3. Registra un usuario de prueba
4. ¡Listo! 🎉

---

## 🔐 OAuth (Opcional)

Solo si quieres autenticación social:

### Google

1. [Google Cloud Console](https://console.cloud.google.com)
2. Crea proyecto > APIs & Services > Credentials
3. OAuth 2.0 Client ID (Web)
4. Redirect URI: `https://[TU_REF].supabase.co/auth/v1/callback`
5. Copia Client ID y Secret
6. Supabase > Authentication > Providers > Google
7. Pega credenciales y guarda

### GitHub

1. [GitHub Settings](https://github.com/settings/developers)
2. New OAuth App
3. Callback: `https://[TU_REF].supabase.co/auth/v1/callback`
4. Copia Client ID y Secret
5. Supabase > Authentication > Providers > GitHub
6. Pega credenciales y guarda

### Facebook

1. [Facebook Developers](https://developers.facebook.com)
2. Create App > Consumer
3. Add Facebook Login
4. Redirect URI: `https://[TU_REF].supabase.co/auth/v1/callback`
5. Copia App ID y Secret
6. Supabase > Authentication > Providers > Facebook
7. Pega credenciales y guarda

---

## 📊 Estructura de la Base de Datos

```
profiles (usuarios)
├── id (UUID)
├── email
├── full_name
└── role ('profesor' o 'estudiante')

exams (exámenes)
├── id
├── title
├── professor_id
├── duration_minutes
├── passing_score
└── access_code

questions (preguntas)
├── id
├── exam_id
├── question_text
└── question_type

question_options (opciones)
├── id
├── question_id
├── option_text
└── is_correct

exam_attempts (intentos)
├── id
├── exam_id
├── student_id
├── score
└── status

question_answers (respuestas)
├── id
├── attempt_id
├── question_id
└── selected_option_id
```

---

## 🎯 Roles y Permisos

### Profesor
- ✅ Crear y editar sus propios exámenes
- ✅ Ver preguntas y opciones de sus exámenes
- ✅ Ver intentos y respuestas en sus exámenes
- ✅ Ver perfiles de estudiantes inscritos
- ❌ No puede ver exámenes de otros profesores

### Estudiante
- ✅ Ver exámenes públicos o con código de acceso
- ✅ Inscribirse a exámenes
- ✅ Realizar exámenes (respetando límite de intentos)
- ✅ Ver sus propios intentos y resultados
- ❌ No puede ver respuestas correctas durante el examen
- ❌ No puede crear o editar exámenes

---

## 🧪 Pruebas Rápidas

### Registro de usuario

```kotlin
// En tu código
viewModel.register(
    email = "test@ejemplo.com",
    password = "test123456",
    fullName = "Usuario Prueba",
    isProfessor = false
)
```

### Inicio de sesión

```kotlin
viewModel.login(
    email = "test@ejemplo.com",
    password = "test123456"
)
```

### Verificar en Supabase

1. **Authentication** > **Users** - Ver usuarios registrados
2. **Table Editor** > **profiles** - Ver perfiles creados
3. **SQL Editor** - Consultar datos:

```sql
-- Ver todos los usuarios
SELECT * FROM profiles;

-- Ver exámenes
SELECT * FROM exams;

-- Ver intentos de examen
SELECT * FROM exam_attempts;
```

---

## 🐛 Problemas Comunes

| Problema | Solución |
|----------|----------|
| "Invalid API key" | Verifica SUPABASE_URL y SUPABASE_ANON_KEY |
| "RLS policy violation" | Ejecuta de nuevo `02_row_level_security.sql` |
| "User not found" | El trigger de perfil falló, se crea manualmente |
| "Network error" | Verifica permisos INTERNET en AndroidManifest |
| OAuth no funciona | Prueba en dispositivo real, no emulador |

---

## 📱 Arquitectura de la App

```
presentation/
├── ui/
│   ├── auth/          # Pantallas de login/registro
│   ├── professor/     # Pantallas de profesor
│   └── student/       # Pantallas de estudiante
└── viewmodels/        # ViewModels

domain/
├── entities/          # Modelos de dominio
├── repositories/      # Interfaces de repositorios
└── usecases/          # Casos de uso

data/
├── models/            # DTOs de Supabase
├── mappers/           # Conversión DTO ↔ Entity
├── datasources/       # Fuentes de datos remotas
└── repositories/      # Implementación de repositorios
```

---

## 🔄 Flujo de Autenticación

```
Usuario ingresa credenciales
        ↓
    UseCase valida
        ↓
    Repository ejecuta
        ↓
    DataSource llama Supabase
        ↓
    Supabase Auth verifica
        ↓
    Trigger crea perfil en profiles
        ↓
    Se retorna User al ViewModel
        ↓
    UI se actualiza
```

---

## 📚 Archivos Clave

| Archivo | Descripción |
|---------|-------------|
| `SupabaseClient.kt` | Cliente de Supabase configurado |
| `AuthRemoteDataSource.kt` | Llamadas a Supabase Auth |
| `AuthRepositoryImpl.kt` | Implementación del repositorio |
| `*UseCase.kt` | Lógica de negocio |
| `*Dto.kt` | Modelos de Supabase |
| `*Mapper.kt` | Conversión de modelos |

---

## 🎓 Casos de Uso Disponibles

### Autenticación
- `LoginUseCase` - Iniciar sesión con email/password
- `RegisterUseCase` - Registrar nuevo usuario
- `LogoutUseCase` - Cerrar sesión
- `SignInWithGoogleUseCase` - Login con Google
- `SignInWithGitHubUseCase` - Login con GitHub
- `SignInWithFacebookUseCase` - Login con Facebook
- `GetCurrentUserUseCase` - Obtener usuario actual
- `IsAuthenticatedUseCase` - Verificar si está autenticado
- `ResetPasswordUseCase` - Recuperar contraseña

### Profesor
- `CreateExamUseCase` - Crear examen
- `GetProfessorExamsUseCase` - Ver mis exámenes
- `GenerateQuestionsWithAIUseCase` - Generar preguntas con IA

### Estudiante
- `JoinExamUseCase` - Inscribirse a examen
- `GetStudentExamHistoryUseCase` - Ver historial

### Examen
- `StartExamUseCase` - Iniciar intento
- `SubmitAnswerUseCase` - Enviar respuesta
- `CompleteExamUseCase` - Completar examen

---

## 🔒 Seguridad

### Row Level Security (RLS)

Todas las tablas tienen RLS habilitado. Los usuarios solo pueden:

- **Profiles**: Ver y editar su propio perfil
- **Exams**: Profesores ven los suyos, estudiantes ven públicos/inscritos
- **Questions**: Según examen accesible
- **Attempts**: Solo sus propios intentos
- **Answers**: Solo sus propias respuestas

### Políticas automáticas

- ✅ Estudiantes no pueden ver respuestas correctas durante examen
- ✅ Límite de intentos respetado por políticas
- ✅ Solo profesores pueden crear exámenes
- ✅ Solo estudiantes pueden realizar exámenes

---

## 🚀 Próximos Pasos

1. ✅ Implementa las pantallas de UI (ya tienes la estructura)
2. ✅ Conecta los ViewModels con los UseCases
3. ✅ Añade validaciones y mensajes de error
4. ✅ Implementa el flujo completo de exámenes
5. ✅ Añade estadísticas y análisis
6. ✅ (Opcional) Integra IA para generar preguntas

---

## 💡 Tips

- Usa el modo offline con Room para caché local
- Implementa Realtime para actualizaciones en vivo
- Añade logs con Timber para debugging
- Usa Coil para cargar avatares de usuarios
- Implementa paginación para listas grandes

---

**¿Listo para empezar? ¡Ejecuta la app y comienza a desarrollar! 🚀**

Para más detalles, consulta `CONFIGURACION_SUPABASE.md`

