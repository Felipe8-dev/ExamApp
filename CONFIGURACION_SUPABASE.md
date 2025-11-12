# 📚 Guía de Configuración Completa - ExamApp con Supabase

Esta guía te llevará paso a paso por todo el proceso de configuración de Supabase para tu aplicación Android de gestión de exámenes educativos.

## 📋 Tabla de Contenidos

1. [Configuración de Supabase](#1-configuración-de-supabase)
2. [Base de Datos](#2-base-de-datos)
3. [Autenticación](#3-autenticación)
4. [OAuth Providers](#4-oauth-providers)
5. [Configuración en Android Studio](#5-configuración-en-android-studio)
6. [Pruebas](#6-pruebas)
7. [Solución de Problemas](#7-solución-de-problemas)

---

## 1. Configuración de Supabase

### 1.1 Crear un proyecto en Supabase

1. Ve a [https://supabase.com](https://supabase.com)
2. Regístrate o inicia sesión
3. Haz clic en **"New Project"**
4. Completa los datos:
   - **Name**: ExamApp (o el nombre que prefieras)
   - **Database Password**: Guarda esta contraseña en un lugar seguro
   - **Region**: Selecciona la región más cercana a tus usuarios
   - **Pricing Plan**: Free (para desarrollo)
5. Haz clic en **"Create new project"**
6. Espera a que el proyecto se cree (puede tomar 1-2 minutos)

### 1.2 Obtener las credenciales

1. Una vez creado el proyecto, ve a **Settings** (⚙️) en el menú lateral
2. Ve a **API**
3. Copia los siguientes valores:
   - **Project URL**: `https://xxxxxx.supabase.co`
   - **anon/public key**: Es una clave JWT larga

4. Guarda estas credenciales, las necesitarás más adelante

---

## 2. Base de Datos

### 2.1 Ejecutar los scripts SQL

1. En el dashboard de Supabase, ve a **SQL Editor** en el menú lateral
2. Haz clic en **"New query"**

#### Script 1: Crear el esquema de base de datos

3. Copia todo el contenido del archivo `supabase/01_database_schema.sql`
4. Pégalo en el editor SQL
5. Haz clic en **"Run"** (▶️)
6. Verifica que no haya errores. Deberías ver el mensaje: "Success. No rows returned"

#### Script 2: Configurar políticas de seguridad (RLS)

7. Crea una nueva query
8. Copia todo el contenido del archivo `supabase/02_row_level_security.sql`
9. Pégalo en el editor SQL
10. Haz clic en **"Run"** (▶️)
11. Verifica que no haya errores

#### Script 3: Datos iniciales y funciones auxiliares

12. Crea una nueva query
13. Copia todo el contenido del archivo `supabase/03_initial_data.sql`
14. Pégalo en el editor SQL
15. Haz clic en **"Run"** (▶️)

### 2.2 Verificar las tablas creadas

1. Ve a **Table Editor** en el menú lateral
2. Deberías ver las siguientes tablas:
   - ✅ profiles
   - ✅ exams
   - ✅ questions
   - ✅ question_options
   - ✅ exam_attempts
   - ✅ question_answers
   - ✅ exam_enrollments
   - ✅ ai_recommendations

3. Haz clic en cada tabla para verificar que las columnas sean correctas

---

## 3. Autenticación

### 3.1 Configurar la autenticación básica

1. Ve a **Authentication** en el menú lateral
2. Ve a **Providers**
3. Verifica que **Email** esté habilitado (debería estar por defecto)

### 3.2 Configurar URLs de redirección

1. En **Authentication**, ve a **URL Configuration**
2. En **Site URL**, ingresa:
   ```
   examapp://auth-callback
   ```

3. En **Redirect URLs**, añade las siguientes URLs (una por línea):
   ```
   examapp://auth-callback
   http://localhost:3000/**
   ```

4. Haz clic en **"Save"**

### 3.3 Configurar el email de confirmación (opcional)

Si quieres desactivar la confirmación de email durante el desarrollo:

1. Ve a **Authentication** > **Providers**
2. Haz clic en **Email**
3. Desactiva **"Confirm email"**
4. Haz clic en **"Save"**

⚠️ **Importante**: En producción, mantén esta opción activada

---

## 4. OAuth Providers

### 4.1 Configurar Google OAuth

#### Paso 1: Crear un proyecto en Google Cloud

1. Ve a [Google Cloud Console](https://console.cloud.google.com)
2. Crea un nuevo proyecto o selecciona uno existente
3. Habilita la **Google+ API**:
   - Ve a **APIs & Services** > **Library**
   - Busca "Google+ API"
   - Haz clic en **Enable**

#### Paso 2: Crear credenciales OAuth

1. Ve a **APIs & Services** > **Credentials**
2. Haz clic en **"Create Credentials"** > **"OAuth client ID"**
3. Si es la primera vez, configura la pantalla de consentimiento:
   - **User Type**: External
   - **App name**: ExamApp
   - **User support email**: Tu email
   - **Developer contact information**: Tu email
4. Selecciona **"Web application"** como tipo
5. En **Authorized redirect URIs**, añade:
   ```
   https://[TU_PROJECT_REF].supabase.co/auth/v1/callback
   ```
   Reemplaza `[TU_PROJECT_REF]` con tu referencia de proyecto (ejemplo: `foulfpimejnwhktayjrn`)

6. Haz clic en **"Create"**
7. Copia el **Client ID** y **Client Secret**

#### Paso 3: Configurar en Supabase

1. En Supabase, ve a **Authentication** > **Providers**
2. Busca **Google** y haz clic para expandir
3. Activa **"Enable Sign in with Google"**
4. Pega el **Client ID** y **Client Secret**
5. Haz clic en **"Save"**

#### Paso 4: Configurar para Android (adicional)

1. En Google Cloud Console, crea otro **OAuth client ID**
2. Esta vez selecciona **"Android"**
3. Ingresa:
   - **Package name**: `com.example.examapp`
   - **SHA-1 certificate fingerprint**: Obtén tu SHA-1 ejecutando:
     ```bash
     keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
     ```

---

### 4.2 Configurar GitHub OAuth

#### Paso 1: Crear una OAuth App en GitHub

1. Ve a [GitHub Settings](https://github.com/settings/developers)
2. Haz clic en **"New OAuth App"**
3. Completa:
   - **Application name**: ExamApp
   - **Homepage URL**: `https://[TU_PROJECT_REF].supabase.co`
   - **Authorization callback URL**:
     ```
     https://[TU_PROJECT_REF].supabase.co/auth/v1/callback
     ```
4. Haz clic en **"Register application"**
5. Copia el **Client ID**
6. Genera un **Client Secret** y cópialo

#### Paso 2: Configurar en Supabase

1. En Supabase, ve a **Authentication** > **Providers**
2. Busca **GitHub** y haz clic para expandir
3. Activa **"Enable Sign in with GitHub"**
4. Pega el **Client ID** y **Client Secret**
5. Haz clic en **"Save"**

---

### 4.3 Configurar Facebook OAuth

#### Paso 1: Crear una App en Facebook

1. Ve a [Facebook Developers](https://developers.facebook.com)
2. Haz clic en **"Create App"**
3. Selecciona **"Consumer"** como tipo de app
4. Completa:
   - **App name**: ExamApp
   - **App contact email**: Tu email
5. Haz clic en **"Create App"**

#### Paso 2: Configurar Facebook Login

1. En el dashboard de tu app, añade el producto **"Facebook Login"**
2. Selecciona **"Web"** como plataforma
3. En **Settings** > **Basic**:
   - Copia el **App ID** y **App Secret**
4. Ve a **Facebook Login** > **Settings**
5. En **Valid OAuth Redirect URIs**, añade:
   ```
   https://[TU_PROJECT_REF].supabase.co/auth/v1/callback
   ```

#### Paso 3: Configurar en Supabase

1. En Supabase, ve a **Authentication** > **Providers**
2. Busca **Facebook** y haz clic para expandir
3. Activa **"Enable Sign in with Facebook"**
4. Pega el **App ID** como Client ID
5. Pega el **App Secret** como Client Secret
6. Haz clic en **"Save"**

---

## 5. Configuración en Android Studio

### 5.1 Actualizar las credenciales de Supabase

1. Abre el archivo `app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt`

2. Reemplaza las constantes con tus credenciales:

```kotlin
private const val SUPABASE_URL = "https://TU_PROJECT_REF.supabase.co"
private const val SUPABASE_ANON_KEY = "TU_ANON_KEY_AQUI"
```

### 5.2 Verificar dependencias

Las dependencias ya están configuradas en `app/build.gradle.kts`. Verifica que estén presentes:

```kotlin
// Supabase
implementation("io.github.jan-tennert.supabase:postgrest-kt:2.1.3")
implementation("io.github.jan-tennert.supabase:gotrue-kt:2.1.3")
implementation("io.github.jan-tennert.supabase:realtime-kt:2.1.3")

// Networking
implementation("io.ktor:ktor-client-android:2.3.7")
implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
```

### 5.3 Sincronizar Gradle

1. Haz clic en **"Sync Now"** en Android Studio
2. Espera a que se descarguen todas las dependencias
3. Verifica que no haya errores de compilación

### 5.4 Configurar el esquema de deep linking

Ya está configurado en `AndroidManifest.xml`:

```xml
<data
    android:scheme="examapp"
    android:host="auth-callback" />
```

Si cambias el nombre del esquema, asegúrate de actualizarlo también en:
- `SupabaseClient.kt` (línea con `scheme = "examapp"`)
- Las configuraciones de OAuth en cada proveedor

---

## 6. Pruebas

### 6.1 Probar el registro con email

1. Ejecuta la aplicación en un emulador o dispositivo
2. Navega a la pantalla de registro
3. Ingresa:
   - **Email**: `test@ejemplo.com`
   - **Contraseña**: `test123456`
   - **Nombre**: `Usuario de Prueba`
   - **Rol**: Estudiante
4. Haz clic en **"Registrarse"**
5. Verifica en Supabase:
   - Ve a **Authentication** > **Users**
   - Deberías ver el nuevo usuario
   - Ve a **Table Editor** > **profiles**
   - Deberías ver el perfil creado

### 6.2 Probar el inicio de sesión

1. Cierra sesión en la app
2. Intenta iniciar sesión con las credenciales anteriores
3. Verifica que puedas acceder a la app

### 6.3 Probar OAuth (Google, GitHub, Facebook)

1. Haz clic en **"Iniciar sesión con Google"** (o el proveedor que configuraste)
2. Deberías ser redirigido al navegador
3. Autoriza la aplicación
4. Deberías volver a la app automáticamente
5. Verifica que tu sesión esté iniciada

⚠️ **Nota**: OAuth puede no funcionar en emuladores. Prueba en un dispositivo real.

---

## 7. Solución de Problemas

### Error: "Invalid API key"

**Causa**: Las credenciales de Supabase no son correctas

**Solución**:
1. Verifica que hayas copiado correctamente `SUPABASE_URL` y `SUPABASE_ANON_KEY`
2. Asegúrate de no tener espacios extra al inicio o final
3. Verifica que el proyecto de Supabase esté activo

### Error: "Row Level Security policy violation"

**Causa**: Las políticas RLS no están configuradas correctamente

**Solución**:
1. Ve a Supabase SQL Editor
2. Ejecuta nuevamente el script `02_row_level_security.sql`
3. Verifica en **Authentication** > **Policies** que las políticas estén activas

### Error: "User not found in profiles table"

**Causa**: El trigger para crear perfiles automáticamente no funcionó

**Solución**:
1. Verifica que el trigger esté creado:
   ```sql
   SELECT * FROM pg_trigger WHERE tgname = 'on_auth_user_created';
   ```
2. Si no existe, ejecuta nuevamente la parte del trigger en `01_database_schema.sql`
3. Como alternativa temporal, el código intenta crear el perfil manualmente

### OAuth no funciona en Android

**Causas posibles**:
- El deep link no está configurado correctamente
- Las URLs de redirección no coinciden
- OAuth no funciona en emuladores (algunas veces)

**Soluciones**:
1. Verifica que `examapp://auth-callback` esté en las URLs permitidas en Supabase
2. Prueba en un dispositivo real en lugar de emulador
3. Verifica los logs de Android con `adb logcat | grep Supabase`

### Error: "Network error"

**Causa**: Problemas de conectividad o permisos

**Solución**:
1. Verifica que tengas los permisos en `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
   ```
2. Verifica tu conexión a internet
3. Si usas un emulador, verifica que tenga acceso a internet

### Los correos de verificación no llegan

**Solución temporal para desarrollo**:
1. Ve a **Authentication** > **Providers** > **Email**
2. Desactiva **"Confirm email"**
3. Guarda los cambios

**Solución para producción**:
- Configura un servidor SMTP personalizado en **Project Settings** > **Auth**

---

## 🎯 Próximos Pasos

Una vez completada la configuración:

1. ✅ Implementa las pantallas de UI de autenticación
2. ✅ Crea el flujo de profesores (crear/editar exámenes)
3. ✅ Crea el flujo de estudiantes (realizar exámenes)
4. ✅ Implementa el sistema de puntuación y resultados
5. ✅ Añade funcionalidades de IA (opcional)

---

## 📚 Recursos Adicionales

- [Documentación de Supabase](https://supabase.com/docs)
- [Supabase Android Kotlin SDK](https://github.com/supabase-community/supabase-kt)
- [Row Level Security en PostgreSQL](https://supabase.com/docs/guides/auth/row-level-security)
- [OAuth con Supabase](https://supabase.com/docs/guides/auth/social-login)

---

## 🆘 ¿Necesitas ayuda?

Si encuentras algún problema no cubierto en esta guía:

1. Revisa los logs de Android Studio
2. Revisa los logs en Supabase (Settings > Logs)
3. Consulta la documentación oficial de Supabase
4. Busca en Stack Overflow con el tag `supabase` y `android`

---

**¡Configuración completada! 🎉**

Ahora tu aplicación ExamApp está lista para usar Supabase como backend con autenticación completa.

