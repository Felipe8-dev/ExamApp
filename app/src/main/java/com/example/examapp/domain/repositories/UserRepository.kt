package com.example.examapp.domain.repositories

import com.example.examapp.domain.entities.User
import com.example.examapp.domain.entities.UserType

interface UserRepository {
    suspend fun createUser(user: User): Result<User>
    suspend fun getUserById(userId: String): Result<User>
    suspend fun getUserByEmail(email: String): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun logoutUser(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
}