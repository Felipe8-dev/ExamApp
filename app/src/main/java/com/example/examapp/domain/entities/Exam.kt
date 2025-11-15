package com.example.examapp.domain.entities

import java.time.LocalDateTime

data class Exam(
    val id: String,
    val title: String,
    val description: String?,
    val professorId: String,
    val subject: String,
    val durationMinutes: Int,
    val passingScore: Double,
    val isPublic: Boolean = false,
    val accessCode: String?,
    val shuffleQuestions: Boolean = false,
    val showResultsImmediately: Boolean = true,
    val allowReview: Boolean = true,
    val maxAttempts: Int = 1,
    val availableFrom: LocalDateTime? = null,
    val availableUntil: LocalDateTime? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime,
    val status: ExamStatus,
    val questions: List<Question> = emptyList()
)