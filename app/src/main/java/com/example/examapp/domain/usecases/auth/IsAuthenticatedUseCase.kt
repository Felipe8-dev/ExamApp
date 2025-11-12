package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.repositories.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso para verificar si el usuario está autenticado
 */
class IsAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Verifica si hay un usuario autenticado
     * @return true si está autenticado, false en caso contrario
     */
    suspend operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}

