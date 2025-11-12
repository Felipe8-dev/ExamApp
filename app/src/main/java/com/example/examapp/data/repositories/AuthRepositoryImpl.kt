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
        try {
            val role = if (isProfessor) "profesor" else "estudiante"
            
            // Registrar usuario
            val signUpResult = authRemoteDataSource.signUpWithEmail(
                email = email,
                password = password,
                fullName = fullName,
                role = role
            )
            
            if (signUpResult.isFailure) {
                emit(Result.failure(signUpResult.exceptionOrNull()!!))
                return@flow
            }
            
            // Esperar un momento para que el trigger cree el perfil
            kotlinx.coroutines.delay(1000)
            
            // Obtener el perfil creado
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                val user = profileResult.getOrNull()!!.toDomain()
                emit(Result.success(user))
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
                        val user = createProfileResult.getOrNull()!!.toDomain()
                        emit(Result.success(user))
                    } else {
                        emit(Result.failure(Exception("Error al crear perfil de usuario")))
                    }
                } else {
                    emit(Result.failure(Exception("Error al obtener ID de usuario")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun signIn(email: String, password: String): Flow<Result<User>> = flow {
        try {
            val signInResult = authRemoteDataSource.signInWithEmail(email, password)
            
            if (signInResult.isFailure) {
                emit(Result.failure(signInResult.exceptionOrNull()!!))
                return@flow
            }
            
            // Obtener perfil
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                val user = profileResult.getOrNull()!!.toDomain()
                emit(Result.success(user))
            } else {
                emit(Result.failure(Exception("No se pudo obtener el perfil del usuario")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun signInWithGoogle(): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.signInWithGoogle()
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun signInWithGitHub(): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.signInWithGitHub()
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun signInWithFacebook(): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.signInWithFacebook()
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun signOut(): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.signOut()
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun getCurrentUser(): Flow<Result<User?>> = flow {
        try {
            val isAuth = authRemoteDataSource.isAuthenticated()
            
            if (!isAuth) {
                emit(Result.success(null))
                return@flow
            }
            
            val profileResult = authRemoteDataSource.getCurrentUserProfile()
            
            if (profileResult.isSuccess) {
                val user = profileResult.getOrNull()?.toDomain()
                emit(Result.success(user))
            } else {
                emit(Result.success(null))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun isAuthenticated(): Boolean {
        return authRemoteDataSource.isAuthenticated()
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.sendPasswordResetEmail(email)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun updatePassword(newPassword: String): Flow<Result<Unit>> = flow {
        try {
            val result = authRemoteDataSource.updatePassword(newPassword)
            emit(result)
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
    
    override suspend fun updateProfile(fullName: String?, avatarUrl: String?): Flow<Result<User>> = flow {
        try {
            val result = authRemoteDataSource.updateUserProfile(fullName, avatarUrl)
            
            if (result.isSuccess) {
                val user = result.getOrNull()!!.toDomain()
                emit(Result.success(user))
            } else {
                emit(Result.failure(result.exceptionOrNull()!!))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

