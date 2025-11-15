package com.example.examapp.data.datasources.remote

import com.example.examapp.data.models.ProfileDto
import com.example.examapp.data.models.ProfileInsertDto
import com.example.examapp.data.network.SupabaseConfig
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.Github
import io.github.jan.supabase.gotrue.providers.Facebook
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data source remoto para autenticación con Supabase
 */
@Singleton
class AuthRemoteDataSource @Inject constructor() {
    
    private val auth = SupabaseConfig.auth
    private val postgrest = SupabaseConfig.postgrest
    
    // ==========================================
    // AUTENTICACIÓN CON EMAIL Y CONTRASEÑA
    // ==========================================
    
    /**
     * Registra un nuevo usuario con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     * @param fullName Nombre completo
     * @param role Rol: "profesor" o "estudiante"
     */
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        fullName: String,
        role: String
    ): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            // Normalizar el role antes de guardarlo
            val normalizedRole = role.trim().lowercase()
            val validRole = when (normalizedRole) {
                "profesor", "professor", "teacher" -> "profesor"
                "estudiante", "student", "alumno" -> "estudiante"
                else -> "estudiante"
            }
            
            // Preparar los metadatos como JsonObject
            val userData = buildJsonObject {
                put("full_name", fullName)
                put("role", validRole)
            }
            
            // Registrar en Supabase Auth
            auth.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
                this.data = userData
            }
            
            // Obtener el usuario después del registro
            // signUpWith puede no retornar el usuario directamente si requiere verificación de email
            val user = auth.currentUserOrNull()
            if (user != null) {
                Result.success(user)
            } else {
                // Si no hay usuario pero no hay error, puede ser que requiera verificación de email
                // En este caso, el usuario se creó pero necesita verificar el email
                // El trigger debería crear el perfil automáticamente
                Result.failure(Exception("Usuario registrado exitosamente. Por favor verifica tu email para completar el proceso."))
            }
        } catch (e: Exception) {
            // Manejar errores específicos
            val errorMessage = when {
                e.message?.contains("already registered") == true ||
                e.message?.contains("already exists") == true ||
                e.message?.contains("duplicate") == true -> 
                    "Este email ya está registrado. Por favor inicia sesión o recupera tu contraseña."
                e.message?.contains("email") == true && e.message?.contains("invalid") == true ->
                    "El email proporcionado no es válido."
                e.message?.contains("password") == true ->
                    "La contraseña no cumple con los requisitos mínimos."
                else -> e.message ?: "Error desconocido al registrar"
            }
            Result.failure(Exception(errorMessage))
        }
    }
    
    /**
     * Inicia sesión con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     */
    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<UserInfo> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
            }
            
            val user = auth.currentUserOrNull()
            if (user != null) {
                Result.success(user)
            } else {
                Result.failure(Exception("No se pudo obtener el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Credenciales inválidas: ${e.message}"))
        }
    }
    
    // ==========================================
    // AUTENTICACIÓN OAUTH
    // ==========================================
    
    /**
     * Inicia el flujo de OAuth con Google
     */
    suspend fun signInWithGoogle(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(Google)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión con Google: ${e.message}"))
        }
    }
    
    /**
     * Inicia el flujo de OAuth con GitHub
     */
    suspend fun signInWithGitHub(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(Github)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión con GitHub: ${e.message}"))
        }
    }
    
    /**
     * Inicia el flujo de OAuth con Facebook
     */
    suspend fun signInWithFacebook(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signInWith(Facebook)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión con Facebook: ${e.message}"))
        }
    }
    
    // ==========================================
    // GESTIÓN DE SESIÓN
    // ==========================================
    
    /**
     * Cierra la sesión del usuario actual
     */
    suspend fun signOut(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al cerrar sesión: ${e.message}"))
        }
    }
    
    /**
     * Obtiene el usuario actual
     */
    suspend fun getCurrentUser(): Result<UserInfo?> = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUserOrNull()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener usuario: ${e.message}"))
        }
    }
    
    /**
     * Verifica si hay una sesión activa
     */
    suspend fun isAuthenticated(): Boolean = withContext(Dispatchers.IO) {
        try {
            auth.currentSessionOrNull() != null
        } catch (e: Exception) {
            false
        }
    }
    
    // ==========================================
    // RECUPERACIÓN DE CONTRASEÑA
    // ==========================================
    
    /**
     * Envía un email para recuperar contraseña
     * @param email Email del usuario
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.resetPasswordForEmail(email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al enviar email de recuperación: ${e.message}"))
        }
    }
    
    /**
     * Actualiza la contraseña del usuario
     * @param newPassword Nueva contraseña
     * Nota: En Supabase, la actualización de contraseña generalmente requiere
     * que el usuario use resetPasswordForEmail primero, o que esté autenticado
     * y use un método específico. Por ahora, esta función está deshabilitada.
     */
    suspend fun updatePassword(newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // En Supabase 2.x, la actualización de contraseña puede requerir
            // un flujo diferente. Por ahora, retornamos un error indicando
            // que se debe usar resetPasswordForEmail
            Result.failure(Exception("La actualización de contraseña debe realizarse mediante resetPasswordForEmail. Use sendPasswordResetEmail primero."))
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar contraseña: ${e.message}"))
        }
    }
    
    // ==========================================
    // PERFIL DE USUARIO
    // ==========================================
    
    /**
     * Obtiene el perfil del usuario actual desde la tabla profiles
     */
    suspend fun getCurrentUserProfile(): Result<ProfileDto> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUserOrNull()?.id 
                ?: return@withContext Result.failure(Exception("Usuario no autenticado"))
            
            val profile = postgrest["profiles"]
                .select(Columns.ALL) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<ProfileDto>()
            
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener perfil: ${e.message}"))
        }
    }
    
    /**
     * Actualiza el perfil del usuario
     */
    suspend fun updateUserProfile(
        fullName: String? = null,
        avatarUrl: String? = null
    ): Result<ProfileDto> = withContext(Dispatchers.IO) {
        try {
            val userId = auth.currentUserOrNull()?.id 
                ?: return@withContext Result.failure(Exception("Usuario no autenticado"))
            
            val updates = mutableMapOf<String, Any?>()
            fullName?.let { updates["full_name"] = it }
            avatarUrl?.let { updates["avatar_url"] = it }
            
            val updatedProfile = postgrest["profiles"]
                .update(updates) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingle<ProfileDto>()
            
            Result.success(updatedProfile)
        } catch (e: Exception) {
            Result.failure(Exception("Error al actualizar perfil: ${e.message}"))
        }
    }
    
    /**
     * Crea un perfil manualmente (por si el trigger no funcionó)
     * Esto normalmente no debería ser necesario
     */
    suspend fun createProfile(
        userId: String,
        email: String,
        fullName: String,
        role: String
    ): Result<ProfileDto> = withContext(Dispatchers.IO) {
        try {
            // Normalizar y validar el role
            val normalizedRole = role.trim().lowercase()
            val validRole = when (normalizedRole) {
                "profesor", "professor", "teacher" -> "profesor"
                "estudiante", "student", "alumno" -> "estudiante"
                else -> {
                    // Si no es un valor reconocido, usar "estudiante" por defecto
                    "estudiante"
                }
            }
            
            val profileInsert = ProfileInsertDto(
                id = userId,
                email = email,
                fullName = fullName,
                role = validRole
            )
            
            // Intentar insertar el perfil
            val profile = postgrest["profiles"]
                .insert(profileInsert)
                .decodeSingle<ProfileDto>()
            
            Result.success(profile)
        } catch (e: Exception) {
            // Si el perfil ya existe, intentar obtenerlo en lugar de fallar
            if (e.message?.contains("duplicate key") == true || 
                e.message?.contains("already exists") == true) {
                try {
                    // El perfil ya existe, intentar obtenerlo
                    val existingProfile = postgrest["profiles"]
                        .select(Columns.ALL) {
                            filter {
                                eq("id", userId)
                            }
                        }
                        .decodeSingle<ProfileDto>()
                    Result.success(existingProfile)
                } catch (getError: Exception) {
                    Result.failure(Exception("El perfil ya existe pero no se pudo obtener: ${getError.message}"))
                }
            } else {
                // Proporcionar un mensaje de error más descriptivo
                val errorMessage = when {
                    e.message?.contains("violates row-level security") == true -> 
                        "No tienes permiso para crear este perfil. Verifica las políticas RLS en Supabase."
                    e.message?.contains("foreign key") == true -> 
                        "El usuario no existe en auth.users"
                    e.message?.contains("check constraint") == true ->
                        "El valor del role no es válido. Debe ser 'profesor' o 'estudiante'."
                    else -> e.message ?: "Error desconocido al crear perfil"
                }
                Result.failure(Exception("Error al crear perfil: $errorMessage"))
            }
        }
    }
    
    // ==========================================
    // VERIFICACIÓN DE EMAIL
    // ==========================================
    
    /**
     * Reenvía el email de verificación
     */
    suspend fun resendVerificationEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.resendEmail(type = OtpType.Email.SIGNUP, email = email)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al reenviar email: ${e.message}"))
        }
    }
    
    /**
     * Obtiene los metadatos del usuario actual desde Supabase Auth
     * Retorna un mapa con full_name y role si están disponibles
     */
    suspend fun getUserMetadata(): Result<Map<String, String>> = withContext(Dispatchers.IO) {
        try {
            val user = auth.currentUserOrNull()
            if (user == null) {
                return@withContext Result.failure(Exception("Usuario no autenticado"))
            }
            
            val metadata = mutableMapOf<String, String>()
            
            // Intentar obtener metadatos de userMetadata
            try {
                user.userMetadata?.let { meta ->
                    meta["full_name"]?.let { metadata["full_name"] = it.toString() }
                    meta["role"]?.let { metadata["role"] = it.toString() }
                }
            } catch (e: Exception) {
                // Si userMetadata no está disponible, usar valores por defecto
            }
            
            // Normalizar el role si existe
            metadata["role"]?.let { role ->
                val normalizedRole = role.trim().lowercase()
                metadata["role"] = when (normalizedRole) {
                    "profesor", "professor", "teacher" -> "profesor"
                    "estudiante", "student", "alumno" -> "estudiante"
                    else -> "estudiante"
                }
            }
            
            // Si no hay metadatos, usar valores por defecto
            if (metadata.isEmpty()) {
                metadata["full_name"] = "Usuario"
                metadata["role"] = "estudiante"
            } else {
                // Asegurar que siempre haya un role válido
                if (!metadata.containsKey("role")) {
                    metadata["role"] = "estudiante"
                }
            }
            
            Result.success(metadata)
        } catch (e: Exception) {
            Result.failure(Exception("Error al obtener metadatos: ${e.message}"))
        }
    }
}

