package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para recuperar contraseña
 */
class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Envía un email para recuperar la contraseña
     * @param email Email del usuario
     */
    suspend operator fun invoke(email: String): Flow<Result<Unit>> {
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(email.contains("@")) { "El email debe ser válido" }
        
        return authRepository.sendPasswordResetEmail(email)
    }
}

