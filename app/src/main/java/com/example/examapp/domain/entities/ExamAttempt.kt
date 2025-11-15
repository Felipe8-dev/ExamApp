package com.example.examapp.domain.entities

import java.time.LocalDateTime

data class ExamAttempt(
    val id: String,
    val examId: String,
    val studentId: String,
    val status: ExamStatus,
    val score: Double? = null,
    val totalPointsEarned: Double? = null,
    val totalPointsPossible: Double? = null,
    val passed: Boolean = false,
    val startedAt: LocalDateTime,
    val completedAt: LocalDateTime? = null,
    val timeSpentSeconds: Int? = null,
    val attemptNumber: Int,
    val answers: List<QuestionAttempt> = emptyList()
)