package com.example.examapp.domain.entities

data class QuestionOption(
    val id: String,
    val questionId: String,
    val optionText: String,
    val isCorrect: Boolean,
    val orderIndex: Int
)