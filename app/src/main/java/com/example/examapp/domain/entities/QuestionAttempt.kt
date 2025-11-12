package com.example.examapp.domain.entities


data class QuestionAttempt(
    val id: String,
    val examAttemptId: String,
    val questionId: String,
    val errorsCount: Int = 0,
    val questionScore: Double,
    val completedAt: String? = null
)