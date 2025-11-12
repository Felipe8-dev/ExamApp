package com.example.examapp.domain.repositories

import com.example.examapp.domain.entities.Question
import com.example.examapp.domain.entities.AIRecommendation

interface AIRepository {
    suspend fun generateQuestions(
        prompt: String,
        numberOfQuestions: Int,
        topics: List<String>
    ): Result<List<Question>>

    suspend fun generateRecommendations(
        incorrectAnswers: List<String>,
        topics: List<String>
    ): Result<List<AIRecommendation>>

    suspend fun analyzeExamResults(
        examAttemptId: String,
        errors: List<String>
    ): Result<List<AIRecommendation>>
}