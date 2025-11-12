package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.entities.User
import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para iniciar sesión con email y contraseña
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Inicia sesión con email y contraseña
     * @param email Email del usuario
     * @param password Contraseña
     * @return Flow con el resultado del login
     */
    suspend operator fun invoke(email: String, password: String): Flow<Result<User>> {
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(email.contains("@")) { "El email debe ser válido" }
        require(password.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
        
        return authRepository.signIn(email, password)
    }
}