# 🔐 CONFIGURAR AUTENTICACIÓN - Paso a Paso

Ya ejecutaste los scripts SQL ✅. Ahora vamos a configurar la autenticación.

---

## 📋 PARTE 1: Configuración Básica en Supabase (2 minutos)

### PASO 1: Configurar URLs de Redirección

1. En tu proyecto de Supabase, ve al menú lateral izquierdo
2. Haz clic en **"Authentication"** (ícono de llave 🔑)
3. Haz clic en **"URL Configuration"**

Verás varios campos:

#### **Site URL:**
```
examapp://auth-callback
```
👆 Escribe esto exactamente

#### **Redirect URLs:** (añade estas líneas, una por una)
```
examapp://auth-callback
http://localhost:3000/**
```

4. Haz clic en **"Save"** al final de la página

---

### PASO 2: Desactivar confirmación de email (para desarrollo)

1. Ve a **"Authentication"** > **"Providers"**
2. Busca **"Email"** en la lista
3. Haz clic en **"Email"** para expandir las opciones
4. Busca el toggle que dice **"Confirm email"**
5. **DESACTÍVALO** (debe quedar en gris/off)
6. Haz clic en **"Save"**

**IMPORTANTE:** ⚠️ En producción, vuelve a activar esto para verificar emails.

---

### PASO 3: Verificar que Email está habilitado

1. En **"Authentication"** > **"Providers"**
2. Verifica que **"Email"** tenga un check verde ✅
3. Si no lo tiene, haz clic en el toggle para activarlo

---

## 🎉 **¡Listo! Autenticación básica configurada**

Ahora puedes registrar usuarios con email y contraseña desde tu app.

---

## 🔵 PARTE 2: Configurar Google OAuth (OPCIONAL - 10 minutos)

Si quieres que los usuarios puedan iniciar sesión con Google:

### PASO 1: Obtener tu Project Reference de Supabase

Necesitas esto para configurar Google:

1. En Supabase, ve a **Settings** ⚙️
2. Ve a **General**
3. Busca **Reference ID**
4. Copia ese ID (ejemplo: `foulfpimejnwhktayjrn`)
5. Guárdalo, lo necesitarás

Tu **Callback URL** será:
```
https://[TU_REFERENCE_ID].supabase.co/auth/v1/callback
```

Ejemplo:
```
https://foulfpimejnwhktayjrn.supabase.co/auth/v1/callback
```

---

### PASO 2: Crear proyecto en Google Cloud Console

1. Ve a: **https://console.cloud.google.com**
2. Inicia sesión con tu cuenta de Google
3. Haz clic en el selector de proyectos (arriba a la izquierda)
4. Haz clic en **"New Project"**
5. Nombre del proyecto: `ExamApp`
6. Haz clic en **"Create"**
7. Espera que se cree (10-20 segundos)
8. Selecciona el proyecto que acabas de crear

---

### PASO 3: Habilitar Google+ API

1. En el menú lateral, ve a **"APIs & Services"**
2. Haz clic en **"Library"**
3. En el buscador, escribe: `Google+ API`
4. Haz clic en **"Google+ API"**
5. Haz clic en el botón **"Enable"**
6. Espera unos segundos

---

### PASO 4: Configurar Pantalla de Consentimiento

1. Ve a **"APIs & Services"** > **"OAuth consent screen"**
2. Selecciona **"External"**
3. Haz clic en **"Create"**

Completa el formulario:

```
App name: ExamApp
User support email: [Tu email de Google]
App logo: [Opcional, déjalo vacío por ahora]

Developer contact information:
Email addresses: [Tu email de Google]
```

4. Haz clic en **"Save and Continue"**
5. En la sección **"Scopes"**, haz clic en **"Save and Continue"** (sin añadir nada)
6. En **"Test users"** (opcional):
   - Haz clic en **"Add Users"**
   - Añade tu email para probar
   - Haz clic en **"Add"**
7. Haz clic en **"Save and Continue"**
8. Haz clic en **"Back to Dashboard"**

---

### PASO 5: Crear Credenciales OAuth

1. Ve a **"APIs & Services"** > **"Credentials"**
2. Haz clic en **"Create Credentials"** (botón arriba)
3. Selecciona **"OAuth client ID"**

Completa:

```
Application type: Web application
Name: ExamApp Web Client
```

En **"Authorized redirect URIs"**:
4. Haz clic en **"Add URI"**
5. Pega tu URL de callback de Supabase:
```
https://[TU_REFERENCE_ID].supabase.co/auth/v1/callback
```

Ejemplo:
```
https://foulfpimejnwhktayjrn.supabase.co/auth/v1/callback
```

6. Haz clic en **"Create"**

---

### PASO 6: Copiar las credenciales

Aparecerá un modal con tus credenciales:

```
Client ID: 123456789-xxxxxxxxx.apps.googleusercontent.com
Client Secret: GOCSPX-xxxxxxxxxxxxx
```

**¡CÓPIALAS Y GUÁRDALAS!** ✅

Si cierras el modal, puedes verlas de nuevo haciendo clic en el nombre de tu OAuth client.

---

### PASO 7: Configurar en Supabase

1. Vuelve a tu proyecto de Supabase
2. Ve a **"Authentication"** > **"Providers"**
3. Busca **"Google"** en la lista
4. Haz clic en **"Google"** para expandir

Completa:

```
Enable Sign in with Google: ✅ ACTIVAR el toggle

Google Client ID: [Pega el Client ID de Google]
Google Client Secret: [Pega el Client Secret de Google]
```

5. Haz clic en **"Save"**

---

## 🎉 **¡Google OAuth configurado!**

Ahora los usuarios pueden iniciar sesión con su cuenta de Google.

---

## 🐙 PARTE 3: Configurar GitHub OAuth (OPCIONAL - 5 minutos)

### PASO 1: Crear OAuth App en GitHub

1. Ve a: **https://github.com/settings/developers**
2. Haz clic en **"OAuth Apps"**
3. Haz clic en **"New OAuth App"**

Completa:

```
Application name: ExamApp

Homepage URL: 
https://[TU_REFERENCE_ID].supabase.co

Application description: [Opcional]
Sistema de gestión de exámenes educativos

Authorization callback URL:
https://[TU_REFERENCE_ID].supabase.co/auth/v1/callback
```

4. Haz clic en **"Register application"**

---

### PASO 2: Copiar credenciales

En la página de tu OAuth App:

1. Copia el **Client ID**
2. Haz clic en **"Generate a new client secret"**
3. Confirma tu contraseña si te lo pide
4. Copia el **Client Secret** (solo se muestra una vez)

**¡GUÁRDALOS!** ✅

---

### PASO 3: Configurar en Supabase

1. Vuelve a Supabase
2. Ve a **"Authentication"** > **"Providers"**
3. Busca **"GitHub"**
4. Haz clic para expandir

Completa:

```
Enable Sign in with GitHub: ✅ ACTIVAR

GitHub Client ID: [Pega el Client ID]
GitHub Client Secret: [Pega el Client Secret]
```

5. Haz clic en **"Save"**

---

## 🎉 **¡GitHub OAuth configurado!**

---

## 📘 PARTE 4: Configurar Facebook OAuth (OPCIONAL - 10 minutos)

### PASO 1: Crear App en Facebook Developers

1. Ve a: **https://developers.facebook.com**
2. Inicia sesión con tu cuenta de Facebook
3. Haz clic en **"My Apps"** (arriba derecha)
4. Haz clic en **"Create App"**

---

### PASO 2: Seleccionar tipo de app

1. Selecciona **"Consumer"**
2. Haz clic en **"Next"**

Completa:

```
App name: ExamApp
App contact email: [Tu email]
```

3. Haz clic en **"Create app"**
4. Puede pedirte verificación de seguridad

---

### PASO 3: Añadir Facebook Login

1. En el dashboard de tu app, busca **"Add a product"**
2. Busca **"Facebook Login"**
3. Haz clic en **"Set Up"**
4. Selecciona **"Web"** como plataforma
5. Salta los pasos de configuración (haz clic en Next)

---

### PASO 4: Configurar Facebook Login Settings

1. En el menú lateral, ve a **"Facebook Login"** > **"Settings"**

En **Valid OAuth Redirect URIs**, añade:

```
https://[TU_REFERENCE_ID].supabase.co/auth/v1/callback
```

2. Haz clic en **"Save Changes"**

---

### PASO 5: Copiar credenciales

1. En el menú lateral, ve a **"Settings"** > **"Basic"**
2. Copia el **App ID**
3. Haz clic en **"Show"** junto a **App Secret**
4. Confirma tu contraseña de Facebook
5. Copia el **App Secret**

**¡GUÁRDALOS!** ✅

---

### PASO 6: Configurar en Supabase

1. Vuelve a Supabase
2. Ve a **"Authentication"** > **"Providers"**
3. Busca **"Facebook"**
4. Haz clic para expandir

Completa:

```
Enable Sign in with Facebook: ✅ ACTIVAR

Facebook Client ID: [Pega el App ID]
Facebook Client Secret: [Pega el App Secret]
```

5. Haz clic en **"Save"**

---

### PASO 7: Hacer la app pública (para producción)

Para desarrollo, ya funciona. Para producción:

1. En Facebook Developers, ve al dashboard de tu app
2. Arriba verás un toggle que dice **"App Mode: Development"**
3. Cambia el toggle a **"Live"**
4. Completa la información requerida (política de privacidad, etc.)

---

## 🎉 **¡Facebook OAuth configurado!**

---

## 📱 PARTE 5: Actualizar Credenciales en Android (2 minutos)

### PASO 1: Copiar credenciales de Supabase

1. En Supabase, ve a **Settings** ⚙️
2. Ve a **API**
3. Copia:

```
Project URL: https://xxxxxxxxx.supabase.co
anon public key: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

### PASO 2: Actualizar en Android Studio

1. Abre Android Studio
2. Abre el archivo:

```
app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt
```

3. Busca las líneas 25-26:

```kotlin
private const val SUPABASE_URL = "https://foulfpimejnwhktayjrn.supabase.co"
private const val SUPABASE_ANON_KEY = "eyJhbGc..."
```

4. **REEMPLAZA** con TUS credenciales:

```kotlin
private const val SUPABASE_URL = "https://TU_PROJECT_URL.supabase.co"
private const val SUPABASE_ANON_KEY = "TU_ANON_KEY_COMPLETA_AQUI"
```

5. Guarda el archivo (Ctrl+S)
6. Haz clic en **"Sync Now"** si aparece

---

## ✅ VERIFICAR CONFIGURACIÓN

### En Supabase:

```
Authentication > Providers:
✅ Email - Habilitado
✅ Google - Habilitado (si lo configuraste)
✅ GitHub - Habilitado (si lo configuraste)
✅ Facebook - Habilitado (si lo configuraste)

Authentication > URL Configuration:
✅ Site URL: examapp://auth-callback
✅ Redirect URLs: examapp://auth-callback
```

### En Android:

```
SupabaseClient.kt:
✅ SUPABASE_URL actualizada
✅ SUPABASE_ANON_KEY actualizada
✅ Gradle sincronizado sin errores
```

---

## 🧪 PROBAR LA AUTENTICACIÓN

### Desde tu app (cuando implementes las pantallas):

#### **Registro con Email:**
```
Email: test@ejemplo.com
Password: test123456
Nombre: Usuario Prueba
Rol: Estudiante
```

#### **Login con Google:**
1. Click en botón "Continuar con Google"
2. Se abre el navegador
3. Seleccionas tu cuenta de Google
4. Autorizas la app
5. Vuelve a la app automáticamente

#### **Login con GitHub:**
1. Click en botón "Continuar con GitHub"
2. Se abre el navegador
3. Autorizas la app
4. Vuelve a la app

#### **Login con Facebook:**
1. Click en botón "Continuar con Facebook"
2. Se abre el navegador
3. Autorizas la app
4. Vuelve a la app

---

### Verificar en Supabase:

1. Ve a **Authentication** > **Users**
2. Deberías ver los usuarios registrados
3. Para cada usuario, verás:
   - Email
   - Provider (email, google, github, facebook)
   - Fecha de creación

4. Ve a **Table Editor** > **profiles**
5. Deberías ver los perfiles creados automáticamente

---

## 🆘 PROBLEMAS COMUNES

### "redirect_uri_mismatch" en Google

**Causa:** La URL de callback no coincide

**Solución:**
1. Verifica que en Google Cloud Console tengas exactamente:
   ```
   https://[TU_REF].supabase.co/auth/v1/callback
   ```
2. Sin espacios, sin slash al final
3. Guarda los cambios

---

### "The redirect_uri MUST match the registered callback URL" en GitHub

**Causa:** URL de callback incorrecta

**Solución:**
1. Ve a GitHub OAuth App Settings
2. Verifica que Authorization callback URL sea:
   ```
   https://[TU_REF].supabase.co/auth/v1/callback
   ```

---

### "Can't Load URL" en Facebook

**Causa:** OAuth Redirect URI no configurada

**Solución:**
1. Ve a Facebook Login > Settings
2. Añade en Valid OAuth Redirect URIs:
   ```
   https://[TU_REF].supabase.co/auth/v1/callback
   ```
3. Save Changes

---

### OAuth no funciona en emulador

**Causa:** Los emuladores a veces tienen problemas con deep links

**Solución:**
- Prueba en un **dispositivo real**
- Los deep links funcionan mejor en dispositivos físicos

---

### "Invalid API key" en Android

**Causa:** Credenciales incorrectas en SupabaseClient.kt

**Solución:**
1. Ve a Supabase > Settings > API
2. Copia de nuevo las credenciales
3. Actualiza SupabaseClient.kt
4. Sync Gradle

---

## 📊 RESUMEN DE CONFIGURACIÓN

```
┌─────────────────────────────────────────────────┐
│  AUTENTICACIÓN BÁSICA (OBLIGATORIO)             │
│  ✅ Email habilitado en Supabase                │
│  ✅ URLs de redirección configuradas            │
│  ✅ Confirmación de email desactivada           │
│  ✅ Credenciales actualizadas en Android        │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│  OAUTH (OPCIONAL)                               │
│  📱 Google: ✅ / ⏸️ Omitir                      │
│  📱 GitHub: ✅ / ⏸️ Omitir                      │
│  📱 Facebook: ✅ / ⏸️ Omitir                    │
└─────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────┐
│  ANDROID                                        │
│  ✅ SupabaseClient.kt actualizado               │
│  ✅ AndroidManifest.xml con deep linking        │
└─────────────────────────────────────────────────┘
```

---

## 🎯 ¿QUÉ SIGUE?

Con la autenticación configurada, ahora puedes:

1. ✅ Implementar las pantallas de Login y Registro
2. ✅ Usar `AuthViewModelExample.kt` como referencia
3. ✅ Probar registro e inicio de sesión
4. ✅ Implementar las funcionalidades de Profesor y Estudiante

---

## 📝 NOTAS IMPORTANTES

### Para Desarrollo:
- ✅ Solo configurar Email es suficiente
- ⏸️ OAuth es opcional, puedes añadirlo después

### Para Producción:
- ✅ Activar confirmación de email
- ✅ Configurar dominio personalizado
- ✅ Configurar SMTP personalizado
- ✅ Revisar políticas de privacidad

---

## 🎉 ¡CONFIGURACIÓN COMPLETA!

Tu app ahora puede:
- ✅ Registrar usuarios con email/password
- ✅ Iniciar sesión con email/password
- ✅ (Opcional) Iniciar sesión con Google
- ✅ (Opcional) Iniciar sesión con GitHub
- ✅ (Opcional) Iniciar sesión con Facebook
- ✅ Cerrar sesión
- ✅ Recuperar contraseña
- ✅ Gestionar perfiles
- ✅ Aplicar seguridad por roles

**¡A desarrollar! 🚀**

