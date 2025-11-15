package com.example.examapp.domain.entities

data class Question(
    val id: String,
    val examId: String,
    val questionText: String,
    val questionType: String, // "multiple_choice", "true_false", "open_ended"
    val points: Double,
    val orderIndex: Int,
    val explanation: String? = null,
    val difficulty: String? = null, // "facil", "medio", "dificil"
    val tags: List<String> = emptyList(),
    val options: List<QuestionOption> = emptyList()
)