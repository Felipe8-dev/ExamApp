package com.example.examapp.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * DTO para intento de examen en Supabase
 * Mapea la tabla 'exam_attempts'
 */
@Serializable
data class ExamAttemptDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("student_id")
    val studentId: String,
    
    @SerialName("status")
    val status: String, // "in_progress", "completed", "abandoned", "timed_out"
    
    @SerialName("score")
    val score: Double? = null,
    
    @SerialName("total_points_earned")
    val totalPointsEarned: Double? = null,
    
    @SerialName("total_points_possible")
    val totalPointsPossible: Double? = null,
    
    @SerialName("passed")
    val passed: Boolean? = null,
    
    @SerialName("started_at")
    val startedAt: String,
    
    @SerialName("completed_at")
    val completedAt: String? = null,
    
    @SerialName("time_spent_seconds")
    val timeSpentSeconds: Int? = null,
    
    @SerialName("attempt_number")
    val attemptNumber: Int,
    
    @SerialName("created_at")
    val createdAt: String,
    
    @SerialName("updated_at")
    val updatedAt: String
)

/**
 * DTO para iniciar un intento
 */
@Serializable
data class ExamAttemptInsertDto(
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("student_id")
    val studentId: String,
    
    @SerialName("attempt_number")
    val attemptNumber: Int,
    
    @SerialName("status")
    val status: String = "in_progress"
)

/**
 * DTO para actualizar un intento
 */
@Serializable
data class ExamAttemptUpdateDto(
    @SerialName("status")
    val status: String? = null,
    
    @SerialName("completed_at")
    val completedAt: String? = null,
    
    @SerialName("time_spent_seconds")
    val timeSpentSeconds: Int? = null
)

/**
 * DTO para respuesta de pregunta
 * Mapea la tabla 'question_answers'
 */
@Serializable
data class QuestionAnswerDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("attempt_id")
    val attemptId: String,
    
    @SerialName("question_id")
    val questionId: String,
    
    @SerialName("selected_option_id")
    val selectedOptionId: String? = null,
    
    @SerialName("answer_text")
    val answerText: String? = null,
    
    @SerialName("is_correct")
    val isCorrect: Boolean? = null,
    
    @SerialName("points_earned")
    val pointsEarned: Double? = null,
    
    @SerialName("answered_at")
    val answeredAt: String,
    
    @SerialName("time_spent_seconds")
    val timeSpentSeconds: Int? = null,
    
    @SerialName("created_at")
    val createdAt: String
)

/**
 * DTO para insertar respuesta
 */
@Serializable
data class QuestionAnswerInsertDto(
    @SerialName("attempt_id")
    val attemptId: String,
    
    @SerialName("question_id")
    val questionId: String,
    
    @SerialName("selected_option_id")
    val selectedOptionId: String? = null,
    
    @SerialName("answer_text")
    val answerText: String? = null,
    
    @SerialName("time_spent_seconds")
    val timeSpentSeconds: Int? = null
)

/**
 * DTO para inscripción a examen
 * Mapea la tabla 'exam_enrollments'
 */
@Serializable
data class ExamEnrollmentDto(
    @SerialName("id")
    val id: String,
    
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("student_id")
    val studentId: String,
    
    @SerialName("enrolled_at")
    val enrolledAt: String
)

/**
 * DTO para inscribirse a examen
 */
@Serializable
data class ExamEnrollmentInsertDto(
    @SerialName("exam_id")
    val examId: String,
    
    @SerialName("student_id")
    val studentId: String
)

