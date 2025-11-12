package com.example.examapp.data.datasources.remote

import com.example.examapp.data.models.ProfileDto
import com.example.examapp.data.models.ProfileInsertDto
import com.example.examapp.data.network.SupabaseConfig
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.exception.AuthException
import io.github.jan.supabase.gotrue.providers.Google
import io.github.jan.supabase.gotrue.providers.Github
import io.github.jan.supabase.gotrue.providers.Facebook
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            // Registrar en Supabase Auth
            val response = auth.signUpWith(io.github.jan.supabase.gotrue.providers.builtin.Email) {
                this.email = email
                this.password = password
                
                // Guardar metadatos que se usarán en el trigger para crear el perfil
                data = mapOf(
                    "full_name" to fullName,
                    "role" to role
                )
            }
            
            Result.success(response)
        } catch (e: AuthException) {
            Result.failure(Exception("Error al registrar: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
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
        } catch (e: AuthException) {
            Result.failure(Exception("Credenciales inválidas: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error al iniciar sesión: ${e.message}"))
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
     */
    suspend fun updatePassword(newPassword: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.updateUser {
                password = newPassword
            }
            Result.success(Unit)
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
            val profileInsert = ProfileInsertDto(
                id = userId,
                email = email,
                fullName = fullName,
                role = role
            )
            
            val profile = postgrest["profiles"]
                .insert(profileInsert)
                .decodeSingle<ProfileDto>()
            
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(Exception("Error al crear perfil: ${e.message}"))
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
            auth.resendEmail(email, OtpType.Email.SIGNUP)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al reenviar email: ${e.message}"))
        }
    }
}

