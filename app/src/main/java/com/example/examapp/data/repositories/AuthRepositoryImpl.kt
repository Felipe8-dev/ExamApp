package com.example.examapp.data.repositories

import com.example.examapp.data.datasources.remote.AuthRemoteDataSource
import com.example.examapp.data.mappers.ProfileMapper.toDomain
import com.example.examapp.domain.entities.User
import com.example.examapp.domain.repositories.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de autenticación
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
    
    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        isProfessor: Boolean
    ): Flow<Result<User>> = flow {
        val result = runCatching {
            val role = if (isProfessor) "profesor" else "estudiante"
            
            // Registrar usuario
            val signUpResult = authRemoteDataSource.signUpWithEmail(
                email = email,
                password = password,
                fullName = fullName,
                role = role
            )
            
            if (signUpResult.isFailure) {
                throw signUpResult.exceptionOrNull() ?: Exception("Error al registrar usuario")
            }
            
            // Esperar un momento para que el trigger cree el perfil
            kotlinx.coroutines.delay(1000)
            
            // Obtener el perfil creado
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                profileResult.getOrNull()!!.toDomain()
            } else {
                // Si el perfil no existe, intentar crearlo manualmente
                val userId = signUpResult.getOrNull()?.id
                if (userId != null) {
                    val createProfileResult = authRemoteDataSource.createProfile(
                        userId = userId,
                        email = email,
                        fullName = fullName,
                        role = role
                    )
                    
                    if (createProfileResult.isSuccess) {
                        createProfileResult.getOrNull()!!.toDomain()
                    } else {
                        val errorMessage = createProfileResult.exceptionOrNull()?.message 
                            ?: "Error desconocido al crear perfil"
                        throw Exception("Error al crear perfil de usuario: $errorMessage")
                    }
                } else {
                    throw Exception("Error al obtener ID de usuario")
                }
            }
        }
        
        emit(result.fold(
            onSuccess = { user -> Result.success(user) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun signIn(email: String, password: String): Flow<Result<User>> = flow {
        val result = runCatching {
            val signInResult = authRemoteDataSource.signInWithEmail(email, password)
            
            if (signInResult.isFailure) {
                throw signInResult.exceptionOrNull() ?: Exception("Error al iniciar sesión")
            }
            
            // Obtener el usuario autenticado para acceder a sus metadatos
            val userInfo = signInResult.getOrNull()
            if (userInfo == null) {
                throw Exception("No se pudo obtener información del usuario")
            }
            
            // Obtener perfil
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                profileResult.getOrNull()!!.toDomain()
            } else {
                // Si el perfil no existe, intentar crearlo usando los metadatos del usuario
                // Obtener metadatos del usuario
                val metadataResult = authRemoteDataSource.getUserMetadata()
                val metadata = metadataResult.getOrNull() ?: mapOf(
                    "full_name" to "Usuario",
                    "role" to "estudiante"
                )
                
                val fullName = metadata["full_name"] ?: "Usuario"
                val role = metadata["role"] ?: "estudiante"
                val userEmail = userInfo.email ?: email
                
                // Intentar crear el perfil
                val createProfileResult = authRemoteDataSource.createProfile(
                    userId = userInfo.id,
                    email = userEmail,
                    fullName = fullName,
                    role = role
                )
                
                if (createProfileResult.isSuccess) {
                    createProfileResult.getOrNull()!!.toDomain()
                } else {
                    throw Exception("No se pudo obtener ni crear el perfil del usuario: ${createProfileResult.exceptionOrNull()?.message}")
                }
            }
        }
        
        emit(result.fold(
            onSuccess = { user -> Result.success(user) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.signInWithGoogle().getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun signInWithGitHub(): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.signInWithGitHub().getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun signInWithFacebook(): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.signInWithFacebook().getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun signOut(): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.signOut().getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun getCurrentUser(): Flow<Result<User?>> = flow {
        val result = runCatching {
            val isAuth = authRemoteDataSource.isAuthenticated()
            
            if (!isAuth) {
                return@runCatching null
            }
            
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                profileResult.getOrNull()?.toDomain()
            } else {
                // Si el perfil no existe, intentar crearlo usando los metadatos del usuario
                val userInfoResult = authRemoteDataSource.getCurrentUser()
                val userInfo = userInfoResult.getOrNull()
                
                if (userInfo != null) {
                    // Obtener metadatos del usuario
                    val metadataResult = authRemoteDataSource.getUserMetadata()
                    val metadata = metadataResult.getOrNull() ?: mapOf(
                        "full_name" to "Usuario",
                        "role" to "estudiante"
                    )
                    
                    val fullName = metadata["full_name"] ?: "Usuario"
                    val role = metadata["role"] ?: "estudiante"
                    val userEmail = userInfo.email ?: ""
                    
                    if (userEmail.isNotBlank()) {
                        val createProfileResult = authRemoteDataSource.createProfile(
                            userId = userInfo.id,
                            email = userEmail,
                            fullName = fullName,
                            role = role
                        )
                        
                        createProfileResult.getOrNull()?.toDomain()
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        }
        
        // Emitir el resultado fuera del runCatching
        emit(result.fold(
            onSuccess = { user -> Result.success(user) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun isAuthenticated(): Boolean {
        return authRemoteDataSource.isAuthenticated()
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.sendPasswordResetEmail(email).getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun updatePassword(newPassword: String): Flow<Result<Unit>> = flow {
        val result = runCatching {
            authRemoteDataSource.updatePassword(newPassword).getOrThrow()
        }
        emit(result.fold(
            onSuccess = { Result.success(it) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
    
    override suspend fun updateProfile(fullName: String?, avatarUrl: String?): Flow<Result<User>> = flow {
        val result = runCatching {
            val profileResult = authRemoteDataSource.updateUserProfile(fullName, avatarUrl)
            if (profileResult.isSuccess) {
                profileResult.getOrNull()!!.toDomain()
            } else {
                throw profileResult.exceptionOrNull() ?: Exception("Error al actualizar perfil")
            }
        }
        emit(result.fold(
            onSuccess = { user -> Result.success(user) },
            onFailure = { e -> Result.failure(e) }
        ))
    }
}

