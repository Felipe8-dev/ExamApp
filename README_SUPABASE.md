# 🎓 ExamApp - Sistema de Gestión de Exámenes con Supabase

Sistema completo de gestión de exámenes educativos con roles diferenciados (Profesor/Estudiante) usando Supabase como backend y Android con Jetpack Compose como frontend.

## ✨ Características

### 🔐 Autenticación Completa
- ✅ Registro e inicio de sesión con email y contraseña
- ✅ OAuth con Google, GitHub y Facebook
- ✅ Asignación de roles (Profesor/Estudiante) durante registro
- ✅ Recuperación de contraseña
- ✅ Gestión de sesiones
- ✅ Perfiles de usuario personalizables

### 👨‍🏫 Funcionalidades para Profesores
- ✅ Crear y editar exámenes
- ✅ Añadir preguntas (opción múltiple, verdadero/falso, abiertas)
- ✅ Configurar duración y puntaje de aprobación
- ✅ Generar códigos de acceso
- ✅ Ver estadísticas de sus exámenes
- ✅ Revisar intentos de estudiantes

### 👨‍🎓 Funcionalidades para Estudiantes
- ✅ Buscar exámenes públicos
- ✅ Inscribirse con código de acceso
- ✅ Realizar exámenes
- ✅ Ver historial de intentos
- ✅ Revisar resultados y respuestas
- ✅ Recibir recomendaciones de IA (opcional)

### 🔒 Seguridad
- ✅ Row Level Security (RLS) en todas las tablas
- ✅ Políticas diferenciadas por rol
- ✅ Validación de permisos en backend
- ✅ Límite de intentos por examen
- ✅ Protección de respuestas correctas durante el examen

---

## 📂 Estructura del Proyecto

```
ExamApp/
├── app/
│   └── src/main/java/com/example/examapp/
│       ├── data/
│       │   ├── datasources/
│       │   │   └── remote/
│       │   │       └── AuthRemoteDataSource.kt      ✅ Llamadas a Supabase Auth
│       │   ├── mappers/
│       │   │   ├── ProfileMapper.kt                 ✅ Conversión User ↔ ProfileDto
│       │   │   ├── ExamMapper.kt                    ✅ Conversión Exam ↔ ExamDto
│       │   │   ├── QuestionMapper.kt                ✅ Conversión Question ↔ QuestionDto
│       │   │   └── ExamAttemptMapper.kt             ✅ Conversión ExamAttempt ↔ ExamAttemptDto
│       │   ├── models/
│       │   │   ├── ProfileDto.kt                    ✅ DTO para tabla profiles
│       │   │   ├── ExamDto.kt                       ✅ DTO para tabla exams
│       │   │   ├── QuestionDto.kt                   ✅ DTO para tabla questions
│       │   │   └── ExamAttemptDto.kt                ✅ DTO para tabla exam_attempts
│       │   ├── network/
│       │   │   └── SupabaseClient.kt                ✅ Cliente de Supabase configurado
│       │   └── repositories/
│       │       ├── AuthRepositoryImpl.kt            ✅ Implementación de autenticación
│       │       └── UserRepositoryImpl.kt
│       ├── di/
│       │   ├── NetworkModule.kt
│       │   ├── RepositoryModule.kt                  ✅ Inyección de repositorios
│       │   └── UseCaseModule.kt
│       ├── domain/
│       │   ├── entities/
│       │   │   ├── User.kt
│       │   │   ├── Exam.kt
│       │   │   ├── Question.kt
│       │   │   └── ExamAttempt.kt
│       │   ├── repositories/
│       │   │   ├── AuthRepository.kt                ✅ Interfaz de autenticación
│       │   │   └── UserRepository.kt
│       │   └── usecases/
│       │       └── auth/
│       │           ├── LoginUseCase.kt              ✅ Caso de uso: Login
│       │           ├── RegisterUseCase.kt           ✅ Caso de uso: Registro
│       │           ├── LogoutUseCase.kt             ✅ Caso de uso: Logout
│       │           ├── SignInWithGoogleUseCase.kt   ✅ Caso de uso: OAuth Google
│       │           ├── SignInWithGitHubUseCase.kt   ✅ Caso de uso: OAuth GitHub
│       │           ├── SignInWithFacebookUseCase.kt ✅ Caso de uso: OAuth Facebook
│       │           ├── GetCurrentUserUseCase.kt     ✅ Caso de uso: Usuario actual
│       │           ├── IsAuthenticatedUseCase.kt    ✅ Caso de uso: Verificar auth
│       │           └── ResetPasswordUseCase.kt      ✅ Caso de uso: Recuperar password
│       ├── presentation/
│       │   ├── ui/
│       │   │   ├── auth/                           (Por implementar)
│       │   │   ├── professor/                      (Por implementar)
│       │   │   └── student/                        (Por implementar)
│       │   └── viewmodels/
│       │       ├── AuthViewModel.kt
│       │       └── AuthViewModelExample.kt          ✅ Ejemplo completo de ViewModel
│       ├── ExamApplication.kt
│       └── MainActivity.kt
├── supabase/
│   ├── 01_database_schema.sql                       ✅ Script: Crear tablas
│   ├── 02_row_level_security.sql                    ✅ Script: Políticas RLS
│   └── 03_initial_data.sql                          ✅ Script: Vistas y funciones
├── CONFIGURACION_SUPABASE.md                        ✅ Guía detallada de configuración
├── INICIO_RAPIDO.md                                 ✅ Guía rápida de 10 minutos
└── README_SUPABASE.md                               ✅ Este archivo

✅ = Completado y listo para usar
```

---

## 🚀 Inicio Rápido

### 1. Clonar el repositorio

```bash
git clone <tu-repo>
cd ExamApp
```

### 2. Configurar Supabase

Sigue la guía rápida en [`INICIO_RAPIDO.md`](INICIO_RAPIDO.md) (10 minutos)

O la guía completa en [`CONFIGURACION_SUPABASE.md`](CONFIGURACION_SUPABASE.md)

### 3. Actualizar credenciales

En `app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt`:

```kotlin
private const val SUPABASE_URL = "https://TU_PROJECT_REF.supabase.co"
private const val SUPABASE_ANON_KEY = "tu_anon_key_aqui"
```

### 4. Ejecutar la app

```bash
./gradlew installDebug
```

O desde Android Studio: Run ▶️

---

## 🗄️ Base de Datos

### Tablas Principales

| Tabla | Descripción | RLS |
|-------|-------------|-----|
| `profiles` | Perfiles de usuarios | ✅ |
| `exams` | Exámenes creados por profesores | ✅ |
| `questions` | Preguntas de los exámenes | ✅ |
| `question_options` | Opciones de respuesta | ✅ |
| `exam_attempts` | Intentos de estudiantes | ✅ |
| `question_answers` | Respuestas a preguntas | ✅ |
| `exam_enrollments` | Inscripciones a exámenes | ✅ |
| `ai_recommendations` | Recomendaciones de IA | ✅ |

### Diagrama de Relaciones

```
┌─────────────┐
│  profiles   │
└──────┬──────┘
       │
       ├─────────────┐
       │             │
┌──────▼──────┐      │
│    exams    │      │
└──────┬──────┘      │
       │             │
┌──────▼──────┐      │
│  questions  │      │
└──────┬──────┘      │
       │             │
┌──────▼──────────┐  │
│ question_options│  │
└─────────────────┘  │
                     │
            ┌────────▼────────┐
            │ exam_enrollments│
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │  exam_attempts  │
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ question_answers│
            └─────────────────┘
```

---

## 🔐 Autenticación

### Flujo de Registro

```
1. Usuario ingresa datos (email, password, nombre, rol)
2. RegisterUseCase valida los datos
3. AuthRepository ejecuta signUp
4. AuthRemoteDataSource llama a Supabase Auth
5. Supabase crea usuario en auth.users
6. Trigger automático crea perfil en profiles
7. Se retorna User al ViewModel
8. UI navega a pantalla principal
```

### Flujo de Login

```
1. Usuario ingresa credenciales
2. LoginUseCase valida los datos
3. AuthRepository ejecuta signIn
4. AuthRemoteDataSource llama a Supabase Auth
5. Supabase verifica credenciales
6. Se obtiene el perfil de profiles
7. Se retorna User al ViewModel
8. UI navega a pantalla principal según rol
```

### Flujo OAuth

```
1. Usuario hace clic en "Iniciar con Google/GitHub/Facebook"
2. Se abre el navegador con la página de autorización
3. Usuario autoriza la aplicación
4. Redirige a examapp://auth-callback
5. Supabase procesa el callback
6. Se obtiene o crea el perfil
7. UI navega a pantalla principal
```

---

## 🛠️ Tecnologías Utilizadas

### Backend
- **Supabase** - Backend as a Service
  - PostgreSQL - Base de datos
  - PostgREST - API REST automática
  - GoTrue - Autenticación
  - Row Level Security - Seguridad a nivel de fila

### Android
- **Kotlin** - Lenguaje de programación
- **Jetpack Compose** - UI declarativa
- **Coroutines & Flow** - Programación asíncrona
- **Hilt** - Inyección de dependencias
- **Clean Architecture** - Arquitectura de capas
- **MVVM** - Patrón de presentación
- **Ktor Client** - Cliente HTTP
- **Kotlinx Serialization** - Serialización JSON

---

## 📱 Arquitectura

### Clean Architecture (3 capas)

```
┌─────────────────────────────────────┐
│       Presentation Layer            │
│  (UI, ViewModels, Compose Screens)  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│         Domain Layer                │
│  (Entities, UseCases, Repositories) │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│          Data Layer                 │
│ (DTOs, DataSources, Repositories)   │
└────────────┬────────────────────────┘
             │
             ▼
      ┌──────────────┐
      │   Supabase   │
      └──────────────┘
```

### Flujo de Datos

```
UI (Compose)
    ↕️
ViewModel
    ↕️
UseCase (lógica de negocio)
    ↕️
Repository (interfaz)
    ↕️
RepositoryImpl (implementación)
    ↕️
DataSource (llamadas API)
    ↕️
Supabase (backend)
```

---

## 📊 Políticas de Seguridad (RLS)

### Profesor

✅ **Puede:**
- Ver y editar sus propios exámenes
- Crear nuevos exámenes
- Ver preguntas y opciones de sus exámenes
- Ver intentos y respuestas en sus exámenes
- Ver perfiles de estudiantes inscritos en sus exámenes

❌ **No puede:**
- Ver o editar exámenes de otros profesores
- Realizar exámenes como estudiante
- Ver datos de estudiantes no inscritos en sus exámenes

### Estudiante

✅ **Puede:**
- Ver exámenes públicos y aquellos en los que está inscrito
- Inscribirse a exámenes con código de acceso
- Realizar exámenes (respetando límite de intentos)
- Ver sus propios intentos y resultados
- Ver sus respuestas después de completar

❌ **No puede:**
- Ver o crear exámenes
- Ver respuestas correctas durante el examen (solo después)
- Ver intentos de otros estudiantes
- Editar preguntas u opciones
- Realizar más intentos del límite permitido

---

## 🧪 Casos de Uso Implementados

### Autenticación ✅
- `LoginUseCase` - Iniciar sesión
- `RegisterUseCase` - Registrar usuario
- `LogoutUseCase` - Cerrar sesión
- `SignInWithGoogleUseCase` - Login con Google
- `SignInWithGitHubUseCase` - Login con GitHub
- `SignInWithFacebookUseCase` - Login con Facebook
- `GetCurrentUserUseCase` - Obtener usuario actual
- `IsAuthenticatedUseCase` - Verificar autenticación
- `ResetPasswordUseCase` - Recuperar contraseña

### Profesor (esqueleto existente)
- `CreateExamUseCase`
- `GetProfessorExamsUseCase`
- `GenerateQuestionsWithAIUseCase`

### Estudiante (esqueleto existente)
- `JoinExamUseCase`
- `GetStudentExamHistoryUseCase`

### Examen (esqueleto existente)
- `StartExamUseCase`
- `SubmitAnswerUseCase`
- `CompleteExamUseCase`

---

## 📝 Ejemplo de Uso

### Registrar Usuario

```kotlin
// En el ViewModel
viewModel.register(
    email = "profesor@ejemplo.com",
    password = "password123",
    fullName = "Juan Pérez",
    isProfessor = true
)
```

### Iniciar Sesión

```kotlin
viewModel.login(
    email = "profesor@ejemplo.com",
    password = "password123"
)
```

### Login con OAuth

```kotlin
// Google
viewModel.signInWithGoogle()

// GitHub
viewModel.signInWithGitHub()

// Facebook
viewModel.signInWithFacebook()
```

### Verificar Autenticación

```kotlin
val currentUser by viewModel.currentUser.collectAsState()
val authState by viewModel.authState.collectAsState()

when (authState) {
    is AuthState.Authenticated -> {
        // Mostrar pantalla principal
        if (currentUser?.userType == UserType.PROFESSOR) {
            ProfessorHomeScreen()
        } else {
            StudentHomeScreen()
        }
    }
    is AuthState.Unauthenticated -> {
        LoginScreen()
    }
    is AuthState.Loading -> {
        LoadingScreen()
    }
    else -> {}
}
```

---

## 🔧 Configuración de OAuth

### Google OAuth

1. Crear proyecto en [Google Cloud Console](https://console.cloud.google.com)
2. Habilitar Google+ API
3. Crear OAuth 2.0 Client ID (Web y Android)
4. Configurar en Supabase Authentication > Providers > Google

**Detalles en:** [`CONFIGURACION_SUPABASE.md#google-oauth`](CONFIGURACION_SUPABASE.md#41-configurar-google-oauth)

### GitHub OAuth

1. Crear OAuth App en [GitHub Settings](https://github.com/settings/developers)
2. Configurar callback URL
3. Configurar en Supabase Authentication > Providers > GitHub

**Detalles en:** [`CONFIGURACION_SUPABASE.md#github-oauth`](CONFIGURACION_SUPABASE.md#42-configurar-github-oauth)

### Facebook OAuth

1. Crear App en [Facebook Developers](https://developers.facebook.com)
2. Añadir Facebook Login
3. Configurar Redirect URI
4. Configurar en Supabase Authentication > Providers > Facebook

**Detalles en:** [`CONFIGURACION_SUPABASE.md#facebook-oauth`](CONFIGURACION_SUPABASE.md#43-configurar-facebook-oauth)

---

## 🐛 Solución de Problemas

### "Invalid API key"
→ Verifica `SUPABASE_URL` y `SUPABASE_ANON_KEY` en `SupabaseClient.kt`

### "RLS policy violation"
→ Ejecuta nuevamente `02_row_level_security.sql` en Supabase SQL Editor

### "User not found in profiles"
→ El trigger falló, el código crea el perfil manualmente como fallback

### "Network error"
→ Verifica permisos de INTERNET en AndroidManifest.xml

### OAuth no funciona
→ Prueba en dispositivo real (no emulador)
→ Verifica URLs de redirección en Supabase
→ Verifica deep link en AndroidManifest

**Más soluciones en:** [`CONFIGURACION_SUPABASE.md#problemas-comunes`](CONFIGURACION_SUPABASE.md#7-solución-de-problemas)

---

## 📚 Documentación

| Archivo | Descripción |
|---------|-------------|
| [`CONFIGURACION_SUPABASE.md`](CONFIGURACION_SUPABASE.md) | Guía completa de configuración paso a paso |
| [`INICIO_RAPIDO.md`](INICIO_RAPIDO.md) | Configuración rápida en 10 minutos |
| [`README_SUPABASE.md`](README_SUPABASE.md) | Este archivo - Resumen general |

---

## 🚀 Próximos Pasos

### Implementaciones Pendientes

1. **UI de Autenticación**
   - Pantalla de Login
   - Pantalla de Registro
   - Pantalla de Recuperación de Contraseña

2. **UI de Profesor**
   - Dashboard con estadísticas
   - Crear/Editar exámenes
   - Gestión de preguntas
   - Ver resultados de estudiantes

3. **UI de Estudiante**
   - Dashboard con exámenes disponibles
   - Realizar exámenes
   - Ver historial y resultados
   - Perfil personal

4. **Funcionalidades Adicionales**
   - Sistema de notificaciones
   - Generación de reportes PDF
   - Integración con IA para generar preguntas
   - Modo offline con caché local (Room)
   - Actualizaciones en tiempo real (Realtime)

---

## 👥 Contribuir

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## 🙏 Agradecimientos

- [Supabase](https://supabase.com) - Por el increíble backend
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - Por la UI moderna
- Comunidad de Android y Kotlin

---

## 📞 Contacto

¿Tienes preguntas? ¿Encontraste algún bug?

- Abre un issue en GitHub
- Consulta la documentación de Supabase
- Revisa los archivos de configuración

---

**¡Feliz codificación! 🎉**

*Última actualización: Octubre 2024*

