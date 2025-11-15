package com.example.examapp.domain.entities

import com.example.examapp.data.local.entities.LocalQuestion

data class ExamState(
    val questions: List<LocalQuestion> = emptyList(),
    val shuffledQuestions: List<LocalQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val questionScores: Map<Int, Double> = emptyMap(), // Índice de pregunta -> puntuación
    val blockedOptions: Map<Int, Set<Int>> = emptyMap(), // Índice de pregunta -> opciones bloqueadas
    val questionFailures: Map<Int, Int> = emptyMap(), // Índice de pregunta -> cantidad de fallos
    val isCompleted: Boolean = false,
    val totalScore: Double = 0.0,
    val totalFailures: Int = 0
)

data class ExamResult(
    val totalScore: Double,
    val maxScore: Double = 20.0,
    val grade: Int, // 1-5
    val totalFailures: Int,
    val categoryFailures: Map<String, Int>, // Categoría -> cantidad de fallos
    val recommendations: List<String>
)
