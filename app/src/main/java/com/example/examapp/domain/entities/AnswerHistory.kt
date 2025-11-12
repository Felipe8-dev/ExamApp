package com.example.examapp.domain.entities

data class AnswerHistory(
    val id: String,
    val questionAttemptId: String,
    val selectedOptionId: String,
    val isCorrect: Boolean,
    val answeredAt: String
)