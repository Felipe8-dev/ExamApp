package com.example.examapp.domain.entities

data class Question(
    val id: String,
    val questionText: String,
    val options: List<QuestionOption>,
    val questionOrder: Int,
    val topicCategory: String,
    val createdByAi: Boolean = false
)