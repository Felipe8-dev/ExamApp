package com.example.examapp.domain.entities

data class Exam(
    val id: String,
    val professorId: String,
    val title: String,
    val description: String?,
    val questions: List<Question>,
    val pointsPerQuestion: Double,
    val accessCode: String,
    val isActive: Boolean = true,
    val createdAt: String? = null
)