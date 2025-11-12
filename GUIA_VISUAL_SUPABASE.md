# 🎯 Guía Visual Paso a Paso - Configuración Supabase

## ✅ PASO 1: Crear Proyecto en Supabase

### 1.1 Ir a Supabase
1. Abre tu navegador
2. Ve a: **https://supabase.com**
3. Haz clic en **"Start your project"** o **"Sign In"**

### 1.2 Crear cuenta (si no tienes)
1. Haz clic en **"Sign Up"**
2. Usa tu cuenta de GitHub (recomendado) o email
3. Verifica tu email si es necesario

### 1.3 Crear nuevo proyecto
1. Una vez dentro, haz clic en **"New Project"**
2. Completa el formulario:

```
Organization: [Selecciona o crea una]
Name: ExamApp
Database Password: [Crea una contraseña FUERTE y GUÁRDALA]
              Ejemplo: ExamApp2024!Secure#Pass
Region: [Selecciona la más cercana a ti]
        - South America (São Paulo) si estás en Latinoamérica
        - o la que prefieras
Pricing Plan: Free (para empezar)
```

3. Haz clic en **"Create new project"**
4. ⏳ Espera 1-2 minutos mientras se crea el proyecto

---

## ✅ PASO 2: Obtener Credenciales (1 minuto)

### 2.1 Ir a Settings
1. En el menú lateral izquierdo, busca el ícono de engranaje ⚙️
2. Haz clic en **"Settings"**
3. En el submenú, haz clic en **"API"**

### 2.2 Copiar credenciales

Verás dos secciones importantes:

**Configuration:**
```
Project URL: https://xxxxxxxxxxxxx.supabase.co
              👆 COPIA ESTO
```

**Project API keys:**
```
anon public: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ...
             👆 COPIA ESTA CLAVE COMPLETA (es larga)
```

### 2.3 Guardar credenciales

Abre un bloc de notas y guarda:
```
PROJECT_URL: https://xxxxxxxxxxxxx.supabase.co
ANON_KEY: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ...
```

---

## ✅ PASO 3: Crear Base de Datos (3 minutos)

### 3.1 Abrir SQL Editor
1. En el menú lateral izquierdo, busca el ícono **</> SQL Editor**
2. Haz clic en **"SQL Editor"**
3. Haz clic en el botón **"+ New query"**

### 3.2 Ejecutar Script 1: Tablas

1. Abre el archivo: **`supabase/01_database_schema.sql`**
2. Copia TODO el contenido (Ctrl+A, Ctrl+C)
3. Pégalo en el editor SQL de Supabase
4. Haz clic en el botón **"Run"** (▶️) en la esquina inferior derecha
5. ⏳ Espera unos segundos
6. ✅ Deberías ver: **"Success. No rows returned"**

### 3.3 Ejecutar Script 2: Seguridad

1. Haz clic en **"+ New query"** de nuevo
2. Abre el archivo: **`supabase/02_row_level_security.sql`**
3. Copia TODO el contenido
4. Pégalo en el nuevo editor
5. Haz clic en **"Run"** (▶️)
6. ✅ Espera el mensaje de éxito

### 3.4 Ejecutar Script 3: Funciones

1. Haz clic en **"+ New query"** otra vez
2. Abre el archivo: **`supabase/03_initial_data.sql`**
3. Copia TODO el contenido
4. Pégalo en el editor
5. Haz clic en **"Run"** (▶️)
6. ✅ Espera el mensaje de éxito

### 3.5 Verificar tablas creadas

1. En el menú lateral, haz clic en **"Table Editor"** (ícono de tabla 📊)
2. Deberías ver 8 tablas:
   - ✅ profiles
   - ✅ exams
   - ✅ questions
   - ✅ question_options
   - ✅ exam_attempts
   - ✅ question_answers
   - ✅ exam_enrollments
   - ✅ ai_recommendations

3. Haz clic en cada tabla para verificar que tienen columnas

---

## ✅ PASO 4: Configurar Autenticación (2 minutos)

### 4.1 Ir a Authentication
1. En el menú lateral, haz clic en **"Authentication"** (ícono de llave 🔑)
2. Haz clic en **"URL Configuration"**

### 4.2 Configurar URLs

En la página verás varios campos:

**Site URL:**
```
examapp://auth-callback
```
👆 Escribe esto exactamente

**Redirect URLs:**
```
examapp://auth-callback
http://localhost:3000/**
```
👆 Añade estas dos líneas (una por línea)

Haz clic en **"Save"** al final de la página

### 4.3 Desactivar confirmación de email (para desarrollo)

1. Ve a **"Authentication"** > **"Providers"**
2. Busca **"Email"** en la lista
3. Haz clic en **"Email"** para expandir
4. Busca **"Confirm email"**
5. **Desactiva** el toggle (debe quedar en gris/off)
6. Haz clic en **"Save"**

**IMPORTANTE:** En producción, vuelve a activar esto.

### 4.4 Verificar configuración

1. Ve a **"Authentication"** > **"Providers"**
2. Verifica que **"Email"** esté habilitado (✅ verde)

---

## ✅ PASO 5: Actualizar Credenciales en Android (1 minuto)

### 5.1 Abrir archivo de configuración

En Android Studio, abre:
```
app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt
```

### 5.2 Reemplazar credenciales

Busca estas líneas (alrededor de la línea 25-26):

```kotlin
private const val SUPABASE_URL = "https://foulfpimejnwhktayjrn.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

Reemplázalas con TUS credenciales que copiaste en el PASO 2:

```kotlin
private const val SUPABASE_URL = "https://TU_PROJECT_URL.supabase.co"
private const val SUPABASE_ANON_KEY = "TU_ANON_KEY_AQUI"
```

### 5.3 Guardar y sincronizar

1. Guarda el archivo (Ctrl+S)
2. En Android Studio, haz clic en **"Sync Now"** si aparece
3. O ve a **File** > **Sync Project with Gradle Files**

---

## ✅ PASO 6: Probar la Aplicación (2 minutos)

### 6.1 Ejecutar la app

1. Conecta un dispositivo Android o inicia un emulador
2. En Android Studio, haz clic en el botón **Run** (▶️)
3. Espera a que la app se instale y abra

### 6.2 Registrar un usuario de prueba

En la pantalla de registro (cuando la implementes), ingresa:

```
Email: test@ejemplo.com
Contraseña: test123456
Nombre: Usuario Prueba
Rol: Estudiante
```

Haz clic en **"Registrarse"**

### 6.3 Verificar en Supabase

1. Ve al dashboard de Supabase
2. Ve a **"Authentication"** > **"Users"**
3. ✅ Deberías ver tu usuario registrado
4. Ve a **"Table Editor"** > **"profiles"**
5. ✅ Deberías ver el perfil del usuario creado

---

## 🎉 ¡LISTO! Configuración Base Completada

Ahora tu aplicación está conectada a Supabase y puede:
- ✅ Registrar usuarios
- ✅ Iniciar sesión
- ✅ Gestionar perfiles
- ✅ Aplicar seguridad por roles

---

## 🔥 EXTRA: Configurar OAuth (OPCIONAL)

Si quieres que los usuarios puedan iniciar sesión con Google, GitHub o Facebook, sigue estos pasos adicionales:

### Google OAuth

1. Ve a [Google Cloud Console](https://console.cloud.google.com)
2. Crea un nuevo proyecto
3. Habilita **"Google+ API"**
4. Ve a **"Credentials"** > **"Create Credentials"** > **"OAuth 2.0 Client ID"**
5. Selecciona **"Web application"**
6. En **"Authorized redirect URIs"**, añade:
   ```
   https://TU_PROJECT_REF.supabase.co/auth/v1/callback
   ```
7. Copia el **Client ID** y **Client Secret**
8. En Supabase:
   - Ve a **Authentication** > **Providers**
   - Busca **Google**
   - Activa el toggle
   - Pega el Client ID y Client Secret
   - Haz clic en **"Save"**

### GitHub OAuth

1. Ve a [GitHub Settings](https://github.com/settings/developers)
2. Haz clic en **"New OAuth App"**
3. Completa:
   ```
   Application name: ExamApp
   Homepage URL: https://TU_PROJECT_REF.supabase.co
   Authorization callback URL: https://TU_PROJECT_REF.supabase.co/auth/v1/callback
   ```
4. Haz clic en **"Register application"**
5. Copia el **Client ID**
6. Genera y copia el **Client Secret**
7. En Supabase:
   - Ve a **Authentication** > **Providers**
   - Busca **GitHub**
   - Activa el toggle
   - Pega el Client ID y Client Secret
   - Haz clic en **"Save"**

### Facebook OAuth

1. Ve a [Facebook Developers](https://developers.facebook.com)
2. Haz clic en **"Create App"**
3. Selecciona **"Consumer"**
4. Completa el nombre: ExamApp
5. Añade el producto **"Facebook Login"**
6. En **Settings** > **Basic**, copia **App ID** y **App Secret**
7. En **Facebook Login** > **Settings**, en **"Valid OAuth Redirect URIs"**, añade:
   ```
   https://TU_PROJECT_REF.supabase.co/auth/v1/callback
   ```
8. En Supabase:
   - Ve a **Authentication** > **Providers**
   - Busca **Facebook**
   - Activa el toggle
   - Pega el App ID y App Secret
   - Haz clic en **"Save"**

---

## 🆘 Problemas Comunes

### "Invalid API key"
❌ **Problema:** Las credenciales no son correctas
✅ **Solución:** Verifica que copiaste correctamente el SUPABASE_URL y SUPABASE_ANON_KEY

### "RLS policy violation"
❌ **Problema:** Las políticas de seguridad no se aplicaron
✅ **Solución:** Ejecuta de nuevo el script `02_row_level_security.sql`

### "Network error"
❌ **Problema:** La app no tiene permisos de internet
✅ **Solución:** Verifica que en `AndroidManifest.xml` esté:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### No se crea el perfil del usuario
❌ **Problema:** El trigger no funcionó
✅ **Solución:** El código lo crea automáticamente como fallback, no te preocupes

### OAuth no funciona
❌ **Problema:** Deep linking mal configurado
✅ **Solución:** 
1. Verifica que en `AndroidManifest.xml` esté el intent-filter
2. Prueba en dispositivo real (no emulador)
3. Verifica las URLs de redirección en cada proveedor

---

## 📝 Checklist Final

Antes de empezar a desarrollar, verifica:

**Supabase:**
- [ ] Proyecto creado
- [ ] Credenciales copiadas
- [ ] Script 01 ejecutado (tablas)
- [ ] Script 02 ejecutado (seguridad)
- [ ] Script 03 ejecutado (funciones)
- [ ] 8 tablas visibles en Table Editor
- [ ] Authentication configurado
- [ ] Redirect URLs configuradas

**Android:**
- [ ] SUPABASE_URL actualizada
- [ ] SUPABASE_ANON_KEY actualizada
- [ ] Gradle sincronizado
- [ ] App compila sin errores
- [ ] Permisos de INTERNET en Manifest

**Pruebas:**
- [ ] Registrar usuario de prueba
- [ ] Usuario visible en Authentication > Users
- [ ] Perfil visible en Table Editor > profiles
- [ ] Login funciona correctamente

---

## 🎓 Siguiente Paso: Implementar UI

Con la configuración lista, puedes:

1. Crear las pantallas de Login y Registro
2. Usar `AuthViewModelExample.kt` como referencia
3. Implementar las pantallas de Profesor y Estudiante

¡Todo listo para empezar a desarrollar! 🚀

