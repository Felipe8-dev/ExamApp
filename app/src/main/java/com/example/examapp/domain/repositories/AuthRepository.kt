package com.example.examapp.domain.repositories

import com.example.examapp.domain.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de autenticación
 * Define las operaciones de autenticación disponibles
 */
interface AuthRepository {
    
    /**
     * Registra un nuevo usuario con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     * @param fullName Nombre completo
     * @param isProfessor true si es profesor, false si es estudiante
     */
    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        isProfessor: Boolean
    ): Flow<Result<User>>
    
    /**
     * Inicia sesión con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     */
    suspend fun signIn(email: String, password: String): Flow<Result<User>>
    
    /**
     * Inicia sesión con Google OAuth
     */
    suspend fun signInWithGoogle(): Flow<Result<Unit>>
    
    /**
     * Inicia sesión con GitHub OAuth
     */
    suspend fun signInWithGitHub(): Flow<Result<Unit>>
    
    /**
     * Inicia sesión con Facebook OAuth
     */
    suspend fun signInWithFacebook(): Flow<Result<Unit>>
    
    /**
     * Cierra la sesión del usuario actual
     */
    suspend fun signOut(): Flow<Result<Unit>>
    
    /**
     * Obtiene el usuario actualmente autenticado
     */
    suspend fun getCurrentUser(): Flow<Result<User?>>
    
    /**
     * Verifica si hay un usuario autenticado
     */
    suspend fun isAuthenticated(): Boolean
    
    /**
     * Envía un email para recuperar la contraseña
     * @param email Email del usuario
     */
    suspend fun sendPasswordResetEmail(email: String): Flow<Result<Unit>>
    
    /**
     * Actualiza la contraseña del usuario
     * @param newPassword Nueva contraseña
     */
    suspend fun updatePassword(newPassword: String): Flow<Result<Unit>>
    
    /**
     * Actualiza el perfil del usuario
     * @param fullName Nuevo nombre (opcional)
     * @param avatarUrl Nueva URL del avatar (opcional)
     */
    suspend fun updateProfile(fullName: String? = null, avatarUrl: String? = null): Flow<Result<User>>
}

