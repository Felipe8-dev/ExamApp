package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.entities.User
import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener el usuario actual
 */
class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Obtiene el usuario actualmente autenticado
     * @return Flow con el usuario actual o null si no está autenticado
     */
    suspend operator fun invoke(): Flow<Result<User?>> {
        return authRepository.getCurrentUser()
    }
}

