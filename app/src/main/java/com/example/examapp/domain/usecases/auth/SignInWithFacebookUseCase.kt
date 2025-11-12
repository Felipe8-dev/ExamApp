package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para iniciar sesión con Facebook OAuth
 */
class SignInWithFacebookUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Inicia el flujo de autenticación con Facebook
     */
    suspend operator fun invoke(): Flow<Result<Unit>> {
        return authRepository.signInWithFacebook()
    }
}

