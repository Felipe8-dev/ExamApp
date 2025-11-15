package com.example.examapp.domain.entities

data class User(
    val id: String,
    val email: String,
    val fullName: String,
    val userType: UserType,
    val avatarUrl: String? = null
)