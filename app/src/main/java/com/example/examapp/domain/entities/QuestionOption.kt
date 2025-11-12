package com.example.examapp.domain.entities

data class QuestionOption(
    val id: String,
    val optionText: String,
    val isCorrect: Boolean,
    val optionOrder: Int
)