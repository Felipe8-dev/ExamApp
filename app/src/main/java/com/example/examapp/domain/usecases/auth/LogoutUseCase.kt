package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para cerrar sesión
 */
class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Cierra la sesión del usuario actual
     * @return Flow con el resultado del logout
     */
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return authRepository.signOut()
    }
}