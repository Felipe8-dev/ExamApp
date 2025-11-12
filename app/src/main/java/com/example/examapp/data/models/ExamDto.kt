package com.example.examapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para examen en Supabase
 * Mapea la tabla 'exams'
 */
@Serializable
data class ExamDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("professor_id")
    val professorId: String,
    
    @SerialName("subject")
    val subject: String,
    
    @SerialName("duration_minutes")
    val durationMinutes: Int,
    
    @SerialName("passing_score")
    val passingScore: Double,
    
    @SerialName("is_public")
    val isPublic: Boolean = false,
    
    @SerialName("access_code")
    val accessCode: String? = null,
    
    @SerialName("shuffle_questions")
    val shuffleQuestions: Boolean = false,
    
    @SerialName("show_results_immediately")
    val showResultsImmediately: Boolean = true,
    
    @SerialName("allow_review")
    val allowReview: Boolean = true,
    
    @SerialName("max_attempts")
    val maxAttempts: Int = 1,
    
    @SerialName("available_from")
    val availableFrom: String? = null,
    
    @SerialName("available_until")
    val availableUntil: String? = null,
    
    @SerialName("is_active")
    val isActive: Boolean = true,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
)

/**
 * DTO para crear examen
 */
@Serializable
data class ExamInsertDto(
    @SerialName("title")
    val title: String,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("professor_id")
    val professorId: String,
    
    @SerialName("subject")
    val subject: String,
    
    @SerialName("duration_minutes")
    val durationMinutes: Int,
    
    @SerialName("passing_score")
    val passingScore: Double = 60.0,
    
    @SerialName("is_public")
    val isPublic: Boolean = false,
    
    @SerialName("access_code")
    val accessCode: String? = null,
    
    @SerialName("shuffle_questions")
    val shuffleQuestions: Boolean = false,
    
    @SerialName("show_results_immediately")
    val showResultsImmediately: Boolean = true,
    
    @SerialName("allow_review")
    val allowReview: Boolean = true,
    
    @SerialName("max_attempts")
    val maxAttempts: Int = 1,
    
    @SerialName("available_from")
    val availableFrom: String? = null,
    
    @SerialName("available_until")
    val availableUntil: String? = null
)

/**
 * DTO para actualizar examen
 */
@Serializable
data class ExamUpdateDto(
    @SerialName("title")
    val title: String? = null,
    
    @SerialName("description")
    val description: String? = null,
    
    @SerialName("duration_minutes")
    val durationMinutes: Int? = null,
    
    @SerialName("passing_score")
    val passingScore: Double? = null,
    
    @SerialName("is_public")
    val isPublic: Boolean? = null,
    
    @SerialName("is_active")
    val isActive: Boolean? = null
)

