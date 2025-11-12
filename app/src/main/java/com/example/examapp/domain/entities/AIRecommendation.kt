package com.example.examapp.domain.entities

data class AIRecommendation(
    val id: String,
    val examAttemptId: String,
    val topicCategory: String,
    val recommendationText: String,
    val createdAt: String
)