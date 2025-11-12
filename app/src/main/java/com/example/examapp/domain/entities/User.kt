package com.example.examapp.domain.entities

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val userType: UserType,
    val createdAt: String? = null
)