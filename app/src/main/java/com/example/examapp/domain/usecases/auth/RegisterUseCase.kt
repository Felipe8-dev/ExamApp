package com.example.examapp.domain.usecases.auth

import com.example.examapp.domain.entities.User
import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para registrar un nuevo usuario
 */
class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Registra un nuevo usuario
     * @param email Email del usuario
     * @param password Contraseña
     * @param fullName Nombre completo
     * @param isProfessor true si es profesor, false si es estudiante
     * @return Flow con el resultado del registro
     */
    suspend operator fun invoke(
        email: String,
        password: String,
        fullName: String,
        isProfessor: Boolean
    ): Flow<Result<User>> {
        // Validaciones
        require(email.isNotBlank()) { "El email no puede estar vacío" }
        require(password.isNotBlank()) { "La contraseña no puede estar vacía" }
        require(fullName.isNotBlank()) { "El nombre no puede estar vacío" }
        require(email.contains("@")) { "El email debe ser válido" }
        require(password.length >= 6) { "La contraseña debe tener al menos 6 caracteres" }
        require(fullName.length >= 3) { "El nombre debe tener al menos 3 caracteres" }
        
        return authRepository.signUp(
            email = email,
            password = password,
            fullName = fullName,
            isProfessor = isProfessor
        )
    }
}