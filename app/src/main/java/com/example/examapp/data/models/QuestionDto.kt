package com.example.examapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para pregunta en Supabase
 * Mapea la tabla 'questions'
 */
@Serializable
data class QuestionDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("question_text")
    val questionText: String,
    
    @SerialName("question_type")
    val questionType: String, // "multiple_choice", "true_false", "open_ended"
    
    @SerialName("points")
    val points: Double,
    
    @SerialName("order_index")
    val orderIndex: Int,
    
    @SerialName("explanation")
    val explanation: String? = null,
    
    @SerialName("difficulty")
    val difficulty: String? = null, // "facil", "medio", "dificil"
    
    @SerialName("tags")
    val tags: List<String>? = null,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
)

/**
 * DTO para crear pregunta
 */
@Serializable
data class QuestionInsertDto(
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("question_text")
    val questionText: String,
    
    @SerialName("question_type")
    val questionType: String,
    
    @SerialName("points")
    val points: Double = 1.0,
    
    @SerialName("order_index")
    val orderIndex: Int,
    
    @SerialName("explanation")
    val explanation: String? = null,
    
    @SerialName("difficulty")
    val difficulty: String? = null,
    
    @SerialName("tags")
    val tags: List<String>? = null
)

/**
 * DTO para opción de pregunta
 * Mapea la tabla 'question_options'
 */
@Serializable
data class QuestionOptionDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("question_id")
    val questionId: String,
    
    @SerialName("option_text")
    val optionText: String,
    
    @SerialName("is_correct")
    val isCorrect: Boolean,
    
    @SerialName("order_index")
    val orderIndex: Int,
    
    @SerialName("created_at")
    val createdAt: String
)

/**
 * DTO para crear opción de pregunta
 */
@Serializable
data class QuestionOptionInsertDto(
    @SerialName("question_id")
    val questionId: String,
    
    @SerialName("option_text")
    val optionText: String,
    
    @SerialName("is_correct")
    val isCorrect: Boolean,
    
    @SerialName("order_index")
    val orderIndex: Int
)

/**
 * DTO combinado: Pregunta con sus opciones
 */
@Serializable
data class QuestionWithOptionsDto(
    @SerialName("question")
    val question: QuestionDto,
    
    @SerialName("options")
    val options: List<QuestionOptionDto>
)

