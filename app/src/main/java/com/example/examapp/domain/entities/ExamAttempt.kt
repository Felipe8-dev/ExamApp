package com.example.examapp.domain.entities

data class ExamAttempt(
    val id: String,
    val examId: String,
    val studentId: String,
    val status: ExamStatus,
    val finalScore: Double? = null,
    val totalErrors: Int = 0,
    val startedAt: String,
    val completedAt: String? = null
)