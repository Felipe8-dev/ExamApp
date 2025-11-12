# 🔐 Referencia Rápida de Configuración OAuth

Este documento contiene todas las URLs y configuraciones necesarias para configurar OAuth en tu proyecto.

---

## 📋 Información de tu Proyecto

**Completa estos datos primero:**

```
Project Reference: ___________________ (ej: foulfpimejnwhktayjrn)
Project URL: https://_________________.supabase.co
Anon Key: ________________________________________________
Package Name: com.example.examapp
Deep Link Scheme: examapp://auth-callback
```

---

## 🔗 URLs Importantes

### Supabase

| Elemento | URL |
|----------|-----|
| Dashboard | https://app.supabase.com/project/[TU_REF] |
| Authentication | https://app.supabase.com/project/[TU_REF]/auth/users |
| Table Editor | https://app.supabase.com/project/[TU_REF]/editor |
| SQL Editor | https://app.supabase.com/project/[TU_REF]/sql |

### OAuth Providers

| Provider | Console URL |
|----------|-------------|
| Google | https://console.cloud.google.com |
| GitHub | https://github.com/settings/developers |
| Facebook | https://developers.facebook.com |

---

## 🌐 Callback URLs para OAuth

**IMPORTANTE:** Reemplaza `[TU_REF]` con tu referencia de proyecto de Supabase.

### Para Supabase

```
Site URL:
examapp://auth-callback

Redirect URLs (una por línea):
examapp://auth-callback
http://localhost:3000/**
https://[TU_REF].supabase.co/**
```

### Para Google Cloud Console

```
Authorized redirect URIs:
https://[TU_REF].supabase.co/auth/v1/callback
```

### Para GitHub

```
Authorization callback URL:
https://[TU_REF].supabase.co/auth/v1/callback
```

### Para Facebook

```
Valid OAuth Redirect URIs:
https://[TU_REF].supabase.co/auth/v1/callback
```

---

## 🔑 Credenciales a Recopilar

### Google OAuth

```
Client ID (Web): 
___________________________________________________.apps.googleusercontent.com

Client Secret (Web):
___________________________________________________

Client ID (Android) - Opcional:
___________________________________________________.apps.googleusercontent.com

SHA-1 Fingerprint:
___________________________________________________
```

**Obtener SHA-1 (debug keystore):**
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### GitHub OAuth

```
Client ID:
___________________________________________________

Client Secret:
___________________________________________________
```

### Facebook OAuth

```
App ID:
___________________________________________________

App Secret:
___________________________________________________
```

---

## ⚙️ Configuración en Supabase

### 1. Authentication > URL Configuration

```yaml
Site URL: examapp://auth-callback

Redirect URLs:
  - examapp://auth-callback
  - http://localhost:3000/**
  - https://[TU_REF].supabase.co/**

Additional Redirect URLs: (opcional)
  - Tu dominio personalizado si tienes uno
```

### 2. Authentication > Providers

#### Email Provider

```yaml
Enable Email provider: ✅
Enable email confirmations: ❌ (para desarrollo)
                           ✅ (para producción)
```

#### Google Provider

```yaml
Enable Sign in with Google: ✅
Client ID (for OAuth): [GOOGLE_CLIENT_ID]
Client Secret (for OAuth): [GOOGLE_CLIENT_SECRET]
Authorized Client IDs: [GOOGLE_ANDROID_CLIENT_ID] (opcional)
```

#### GitHub Provider

```yaml
Enable Sign in with GitHub: ✅
Client ID (for OAuth): [GITHUB_CLIENT_ID]
Client Secret (for OAuth): [GITHUB_CLIENT_SECRET]
```

#### Facebook Provider

```yaml
Enable Sign in with Facebook: ✅
Client ID (for OAuth): [FACEBOOK_APP_ID]
Client Secret (for OAuth): [FACEBOOK_APP_SECRET]
```

---

## 📱 Configuración en Android

### AndroidManifest.xml

Ya está configurado con:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    
    <data
        android:scheme="examapp"
        android:host="auth-callback" />
</intent-filter>
```

### SupabaseClient.kt

Actualiza con tus credenciales:

```kotlin
private const val SUPABASE_URL = "https://[TU_REF].supabase.co"
private const val SUPABASE_ANON_KEY = "[TU_ANON_KEY]"
```

---

## ✅ Checklist de Configuración

### Supabase
- [ ] Proyecto creado
- [ ] Scripts SQL ejecutados (01, 02, 03)
- [ ] Credenciales copiadas (URL y Key)
- [ ] Site URL configurada
- [ ] Redirect URLs configuradas
- [ ] Email provider habilitado

### Google OAuth (Opcional)
- [ ] Proyecto creado en Google Cloud
- [ ] Google+ API habilitada
- [ ] Pantalla de consentimiento configurada
- [ ] OAuth Client ID (Web) creado
- [ ] Redirect URI configurada
- [ ] Client ID y Secret copiados
- [ ] Configurado en Supabase

### GitHub OAuth (Opcional)
- [ ] OAuth App creada en GitHub
- [ ] Callback URL configurada
- [ ] Client ID y Secret copiados
- [ ] Configurado en Supabase

### Facebook OAuth (Opcional)
- [ ] App creada en Facebook Developers
- [ ] Facebook Login añadido
- [ ] Valid OAuth Redirect URI configurada
- [ ] App ID y Secret copiados
- [ ] Configurado en Supabase

### Android
- [ ] SUPABASE_URL actualizada
- [ ] SUPABASE_ANON_KEY actualizada
- [ ] Deep link configurado en Manifest
- [ ] Gradle sincronizado
- [ ] App compilando sin errores

---

## 🧪 Testing

### Comandos útiles para testing

#### Verificar permisos en AndroidManifest
```bash
grep "INTERNET" app/src/main/AndroidManifest.xml
```

#### Ver logs de Supabase en Android
```bash
adb logcat | grep -i supabase
```

#### Ver logs de autenticación
```bash
adb logcat | grep -i "auth\|oauth"
```

#### Limpiar datos de la app
```bash
adb shell pm clear com.example.examapp
```

### Pruebas manuales

1. **Registro con Email**
   - [ ] Registrar nuevo usuario
   - [ ] Verificar en Supabase > Authentication > Users
   - [ ] Verificar en Supabase > Table Editor > profiles

2. **Login con Email**
   - [ ] Iniciar sesión con usuario creado
   - [ ] Verificar que la sesión se mantiene
   - [ ] Cerrar sesión correctamente

3. **OAuth Google**
   - [ ] Click en botón de Google
   - [ ] Redirige al navegador
   - [ ] Autoriza la aplicación
   - [ ] Redirige de vuelta a la app
   - [ ] Usuario autenticado

4. **OAuth GitHub**
   - [ ] Click en botón de GitHub
   - [ ] Redirige al navegador
   - [ ] Autoriza la aplicación
   - [ ] Redirige de vuelta a la app
   - [ ] Usuario autenticado

5. **OAuth Facebook**
   - [ ] Click en botón de Facebook
   - [ ] Redirige al navegador
   - [ ] Autoriza la aplicación
   - [ ] Redirige de vuelta a la app
   - [ ] Usuario autenticado

---

## 🔍 Consultas SQL Útiles

### Ver todos los usuarios registrados
```sql
SELECT id, email, full_name, role, created_at
FROM profiles
ORDER BY created_at DESC;
```

### Ver usuarios con su método de autenticación
```sql
SELECT 
    p.id,
    p.email,
    p.full_name,
    p.role,
    u.last_sign_in_at,
    u.confirmation_sent_at
FROM profiles p
JOIN auth.users u ON u.id = p.id
ORDER BY u.last_sign_in_at DESC;
```

### Verificar políticas RLS
```sql
SELECT schemaname, tablename, policyname, permissive, roles, cmd, qual
FROM pg_policies
WHERE schemaname = 'public'
ORDER BY tablename, policyname;
```

### Ver intentos de autenticación fallidos (últimas 24h)
```sql
SELECT 
    created_at,
    factor_id,
    factor_type,
    ip_address,
    user_agent
FROM auth.audit_log_entries
WHERE created_at > NOW() - INTERVAL '24 hours'
AND action = 'login'
ORDER BY created_at DESC;
```

---

## 📞 Troubleshooting por Provider

### Google OAuth

**Error: redirect_uri_mismatch**
- Verifica que la URL en Google Cloud Console sea exactamente:
  `https://[TU_REF].supabase.co/auth/v1/callback`
- Sin espacios, sin slash al final

**Error: access_denied**
- Verifica que la pantalla de consentimiento esté publicada
- Añade tu email como usuario de prueba si está en modo Testing

### GitHub OAuth

**Error: redirect_uri_mismatch**
- Verifica la URL en GitHub OAuth App settings
- Debe ser exactamente: `https://[TU_REF].supabase.co/auth/v1/callback`

**Error: application_suspended**
- Verifica que tu OAuth App esté activa
- Revisa el email asociado a tu cuenta de GitHub

### Facebook OAuth

**Error: Can't Load URL**
- Verifica que Facebook Login esté añadido a tu app
- Verifica las Valid OAuth Redirect URIs
- Asegúrate de que tu app no esté en modo Desarrollo con usuarios limitados

**Error: Invalid Scopes**
- Facebook requiere `email` scope por defecto
- Supabase lo incluye automáticamente

---

## 🚨 Errores Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| `Invalid API key` | Credenciales incorrectas | Verifica SUPABASE_URL y KEY |
| `Network request failed` | Sin conexión a internet | Verifica permisos y conexión |
| `redirect_uri_mismatch` | URL de callback incorrecta | Verifica URLs en provider y Supabase |
| `User not found` | Perfil no creado | El código lo crea automáticamente |
| `RLS policy violation` | Políticas no aplicadas | Re-ejecuta 02_row_level_security.sql |
| `Invalid grant` | Token expirado | Cierra sesión y vuelve a iniciar |
| `Deep link not working` | Intent filter mal configurado | Verifica AndroidManifest.xml |

---

## 📚 Referencias

### Documentación Oficial

- [Supabase Auth](https://supabase.com/docs/guides/auth)
- [Supabase OAuth](https://supabase.com/docs/guides/auth/social-login)
- [Google OAuth](https://developers.google.com/identity/protocols/oauth2)
- [GitHub OAuth](https://docs.github.com/en/developers/apps/building-oauth-apps)
- [Facebook Login](https://developers.facebook.com/docs/facebook-login)

### SDKs

- [Supabase Kotlin](https://github.com/supabase-community/supabase-kt)
- [Ktor Client](https://ktor.io/docs/client.html)

---

## 💾 Guardar esta Configuración

**IMPORTANTE:** Guarda de forma segura:

1. ✅ SUPABASE_URL
2. ✅ SUPABASE_ANON_KEY
3. ✅ Database Password
4. ✅ Google Client ID y Secret
5. ✅ GitHub Client ID y Secret
6. ✅ Facebook App ID y Secret

**NO COMPARTAS** estas credenciales públicamente.

---

**Configuración completada exitosamente! 🎉**

*Última actualización: Octubre 2024*

