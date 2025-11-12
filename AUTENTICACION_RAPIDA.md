# ⚡ CONFIGURAR AUTENTICACIÓN - Versión Rápida

## ✅ PARTE OBLIGATORIA (2 minutos)

### 1️⃣ Configurar URLs en Supabase

```
Supabase → Authentication → URL Configuration

Site URL:
examapp://auth-callback

Redirect URLs:
examapp://auth-callback
http://localhost:3000/**

→ Save
```

### 2️⃣ Desactivar confirmación de email (solo desarrollo)

```
Supabase → Authentication → Providers → Email

Confirm email: ❌ DESACTIVAR

→ Save
```

### 3️⃣ Actualizar credenciales en Android

```
1. Supabase → Settings → API
   Copiar:
   - Project URL
   - anon public key

2. Android Studio → SupabaseClient.kt
   Reemplazar:
   - SUPABASE_URL = "tu URL"
   - SUPABASE_ANON_KEY = "tu key"

3. Sync Gradle
```

## 🎉 ¡LISTO! Ya puedes usar email/password

---

## 🔵 GOOGLE OAUTH (OPCIONAL - 10 min)

### 1️⃣ Google Cloud Console

```
https://console.cloud.google.com

→ New Project: "ExamApp"
→ APIs & Services → Library
→ Buscar "Google+ API" → Enable
```

### 2️⃣ Configurar OAuth

```
→ APIs & Services → OAuth consent screen
→ External → Create
   App name: ExamApp
   Email: tu email
→ Save and Continue (x3)

→ Credentials → Create Credentials → OAuth client ID
   Type: Web application
   Name: ExamApp Web Client
   Authorized redirect URIs:
   https://[TU_REF].supabase.co/auth/v1/callback

→ Copiar Client ID y Client Secret
```

### 3️⃣ Configurar en Supabase

```
Supabase → Authentication → Providers → Google

Enable: ✅
Client ID: [pegar]
Client Secret: [pegar]

→ Save
```

---

## 🐙 GITHUB OAUTH (OPCIONAL - 5 min)

### 1️⃣ GitHub Settings

```
https://github.com/settings/developers

→ OAuth Apps → New OAuth App
   Name: ExamApp
   Homepage: https://[TU_REF].supabase.co
   Callback: https://[TU_REF].supabase.co/auth/v1/callback

→ Register application
→ Copiar Client ID
→ Generate client secret → Copiar
```

### 2️⃣ Configurar en Supabase

```
Supabase → Authentication → Providers → GitHub

Enable: ✅
Client ID: [pegar]
Client Secret: [pegar]

→ Save
```

---

## 📘 FACEBOOK OAUTH (OPCIONAL - 10 min)

### 1️⃣ Facebook Developers

```
https://developers.facebook.com

→ My Apps → Create App
   Type: Consumer
   Name: ExamApp
   Email: tu email

→ Add Facebook Login → Web
```

### 2️⃣ Configurar Login

```
→ Facebook Login → Settings
   Valid OAuth Redirect URIs:
   https://[TU_REF].supabase.co/auth/v1/callback

→ Save Changes

→ Settings → Basic
   Copiar App ID y App Secret
```

### 3️⃣ Configurar en Supabase

```
Supabase → Authentication → Providers → Facebook

Enable: ✅
Client ID: [App ID]
Client Secret: [App Secret]

→ Save
```

---

## 🔍 OBTENER TU REFERENCE ID

```
Supabase → Settings → General → Reference ID

Ejemplo: foulfpimejnwhktayjrn

Tu callback URL será:
https://foulfpimejnwhktayjrn.supabase.co/auth/v1/callback
```

---

## ✅ VERIFICAR TODO

### En Supabase:

```
Authentication → Providers:
✅ Email (verde)
✅ Google (si lo configuraste)
✅ GitHub (si lo configuraste)
✅ Facebook (si lo configuraste)

Authentication → URL Configuration:
✅ Site URL configurada
✅ Redirect URLs configuradas
```

### En Android:

```
SupabaseClient.kt:
✅ SUPABASE_URL actualizada
✅ SUPABASE_ANON_KEY actualizada

Gradle:
✅ Sincronizado sin errores
```

---

## 🧪 PROBAR

### Registro (en tu app):

```
Email: test@ejemplo.com
Password: test123456
Nombre: Usuario Prueba
Rol: Estudiante
```

### Verificar:

```
Supabase → Authentication → Users
✅ Usuario aparece

Supabase → Table Editor → profiles
✅ Perfil creado
```

---

## 🆘 ERRORES COMUNES

| Error | Solución |
|-------|----------|
| redirect_uri_mismatch | Verifica la URL exacta en el proveedor |
| Invalid API key | Copia de nuevo las credenciales de Supabase |
| OAuth no funciona | Prueba en dispositivo real, no emulador |
| Can't Load URL (Facebook) | Configura Valid OAuth Redirect URIs |

---

## 📊 RESUMEN

```
OBLIGATORIO (2 min):
✅ Configurar URLs en Supabase
✅ Desactivar confirmación de email
✅ Actualizar SupabaseClient.kt

OPCIONAL (añadir después):
⏸️ Google OAuth (10 min)
⏸️ GitHub OAuth (5 min)
⏸️ Facebook OAuth (10 min)
```

---

## 🎯 SIGUIENTE PASO

Con la autenticación lista:

```
1. Implementar pantalla de Login
2. Implementar pantalla de Registro
3. Usar AuthViewModelExample.kt como guía
4. Probar registro/login
5. ¡Desarrollar tu app!
```

---

**Guía completa:** `CONFIGURAR_AUTENTICACION.md`

**¡Todo listo para desarrollar! 🚀**

