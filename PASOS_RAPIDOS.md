# 🚀 PASOS RÁPIDOS - Configuración Supabase

## 📍 UBICACIÓN DE ARCHIVOS

Tienes 3 scripts SQL en la carpeta `supabase/`:
```
✅ 01_database_schema.sql       → Crea las tablas
✅ 02_row_level_security.sql    → Configura seguridad
✅ 03_initial_data.sql          → Crea funciones auxiliares
✅ VERIFICACION_PASO_A_PASO.sql → Para verificar todo
```

---

## ⚡ CONFIGURACIÓN EN 5 PASOS

### 📝 PASO 1: Crear Proyecto Supabase

```
1. Ir a: https://supabase.com
2. Sign In (o crear cuenta)
3. Click "New Project"
4. Llenar:
   - Name: ExamApp
   - Password: [Crear una contraseña fuerte]
   - Region: [Más cercana a ti]
5. Click "Create new project"
6. ⏳ Esperar 1-2 minutos
```

---

### 🔑 PASO 2: Copiar Credenciales

```
1. Ir a Settings ⚙️ (menú izquierdo)
2. Click "API"
3. Copiar:
   
   Project URL: https://xxxxx.supabase.co
   anon public: eyJhbGc... (la clave completa)

4. Guardar en un lugar seguro (las necesitarás después)
```

---

### 🗄️ PASO 3: Ejecutar Scripts SQL

```
1. Click "SQL Editor" </> (menú izquierdo)
2. Click "+ New query"

3. SCRIPT 1:
   - Abrir: supabase/01_database_schema.sql
   - Copiar TODO el contenido
   - Pegar en Supabase
   - Click "Run" ▶️
   - ✅ Debe decir "Success. No rows returned"

4. Click "+ New query" de nuevo

5. SCRIPT 2:
   - Abrir: supabase/02_row_level_security.sql
   - Copiar TODO el contenido
   - Pegar en Supabase
   - Click "Run" ▶️
   - ✅ Esperar "Success"

6. Click "+ New query" otra vez

7. SCRIPT 3:
   - Abrir: supabase/03_initial_data.sql
   - Copiar TODO el contenido
   - Pegar en Supabase
   - Click "Run" ▶️
   - ✅ Esperar "Success"
```

---

### 🔐 PASO 4: Configurar Autenticación

```
1. Click "Authentication" 🔑 (menú izquierdo)
2. Click "URL Configuration"
3. En "Site URL" escribir:
   
   examapp://auth-callback

4. En "Redirect URLs" escribir (una por línea):
   
   examapp://auth-callback
   http://localhost:3000/**

5. Click "Save"

6. Ir a "Providers"
7. Click "Email" para expandir
8. DESACTIVAR "Confirm email" (para desarrollo)
9. Click "Save"
```

---

### 📱 PASO 5: Actualizar Android

```
1. Abrir Android Studio
2. Abrir archivo:
   
   app/src/main/java/com/example/examapp/data/network/SupabaseClient.kt

3. Buscar líneas 25-26:
   
   private const val SUPABASE_URL = "..."
   private const val SUPABASE_ANON_KEY = "..."

4. Reemplazar con TUS credenciales del PASO 2:
   
   private const val SUPABASE_URL = "https://tu-proyecto.supabase.co"
   private const val SUPABASE_ANON_KEY = "tu_clave_anon_aqui"

5. Guardar (Ctrl+S)
6. Click "Sync Now" en Android Studio
```

---

## ✅ VERIFICAR INSTALACIÓN

### Opción 1: Verificar en Supabase

```
1. En Supabase, ir a "Table Editor" 📊
2. Deberías ver 8 tablas:
   ✅ profiles
   ✅ exams
   ✅ questions
   ✅ question_options
   ✅ exam_attempts
   ✅ question_answers
   ✅ exam_enrollments
   ✅ ai_recommendations
```

### Opción 2: Ejecutar Script de Verificación

```
1. En Supabase, ir a "SQL Editor"
2. Click "+ New query"
3. Abrir: supabase/VERIFICACION_PASO_A_PASO.sql
4. Copiar todo y pegar
5. Click "Run" ▶️
6. Revisar que todo tenga ✅ OK
```

---

## 🧪 PROBAR LA APP

### Ejecutar aplicación

```
1. En Android Studio: Click Run ▶️
2. Esperar que se instale
```

### Cuando implementes las pantallas de UI:

```
Registro de prueba:
- Email: test@ejemplo.com
- Contraseña: test123456
- Nombre: Usuario Prueba
- Rol: Estudiante

→ Click "Registrarse"
```

### Verificar en Supabase:

```
1. Ir a "Authentication" > "Users"
   ✅ Debe aparecer el usuario

2. Ir a "Table Editor" > "profiles"
   ✅ Debe aparecer el perfil
```

---

## 🆘 SI ALGO FALLA

### Error: "Invalid API key"
```
❌ Problema: Credenciales incorrectas
✅ Solución: 
   1. Ir a Supabase > Settings > API
   2. Copiar de nuevo las credenciales
   3. Actualizar SupabaseClient.kt
```

### Error: "RLS policy violation"
```
❌ Problema: Seguridad no configurada
✅ Solución:
   1. Ejecutar de nuevo: 02_row_level_security.sql
   2. Verificar en "Table Editor" que las tablas existan
```

### No se ve ninguna tabla
```
❌ Problema: Scripts no ejecutados
✅ Solución:
   1. Ir a SQL Editor
   2. Ejecutar en orden: 01, 02, 03
   3. Verificar mensajes de éxito
```

### App no compila
```
❌ Problema: Gradle no sincronizado
✅ Solución:
   1. File > Invalidate Caches and Restart
   2. Esperar que reinicie
   3. Click "Sync Now"
```

---

## 📋 CHECKLIST COMPLETO

**Antes de ejecutar la app:**

```
Supabase:
□ Proyecto creado
□ Credenciales copiadas
□ Script 01 ejecutado
□ Script 02 ejecutado  
□ Script 03 ejecutado
□ 8 tablas visibles
□ Authentication configurado
□ Redirect URLs configuradas

Android:
□ SUPABASE_URL actualizada
□ SUPABASE_ANON_KEY actualizada
□ Gradle sincronizado
□ App compila sin errores
```

---

## 🎯 ¿QUÉ SIGUE?

Una vez completado todo:

```
1. ✅ Backend configurado
2. ✅ Base de datos lista
3. ✅ Autenticación funcionando
4. ✅ Seguridad implementada

AHORA PUEDES:
→ Implementar las pantallas de UI
→ Usar AuthViewModelExample.kt como referencia
→ Conectar las pantallas con los ViewModels
→ ¡Desarrollar tu app!
```

---

## 📚 DOCUMENTACIÓN ADICIONAL

```
GUIA_VISUAL_SUPABASE.md        → Guía con imágenes y detalles
CONFIGURACION_SUPABASE.md      → Configuración completa
INICIO_RAPIDO.md               → Inicio rápido
README_SUPABASE.md             → Documentación general
supabase/OAUTH_REFERENCE.md   → Configurar Google/GitHub/Facebook
```

---

## 🎉 ¡LISTO!

```
Si completaste todos los pasos:
→ Tu backend está 100% configurado
→ Tu app puede registrar y autenticar usuarios
→ Todo está listo para desarrollar

¡A programar! 💻🚀
```

