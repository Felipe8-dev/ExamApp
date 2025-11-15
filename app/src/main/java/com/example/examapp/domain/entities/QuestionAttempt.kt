package com.example.examapp.domain.entities

data class QuestionAttempt(
    val id: String,
    val attemptId: String,
    val questionId: String,
    val selectedOptionId: String? = null,
    val answerText: String? = null,
    val isCorrect: Boolean? = null,
    val pointsEarned: Double = 0.0,
    val timeSpentSeconds: Int? = null
)