package com.example.examapp.data.mappers

import com.example.examapp.data.models.ExamAttemptDto
import com.example.examapp.data.models.ExamAttemptInsertDto
import com.example.examapp.data.models.QuestionAnswerDto
import com.example.examapp.data.models.QuestionAnswerInsertDto
import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.entities.ExamStatus
import com.example.examapp.domain.entities.QuestionAttempt
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Mapper para convertir entre ExamAttemptDto y ExamAttempt (entidad de dominio)
 */
object ExamAttemptMapper {
    
    /**
     * Convierte ExamAttemptDto a ExamAttempt
     */
    fun ExamAttemptDto.toDomain(): ExamAttempt {
        return ExamAttempt(
            id = id,
            examId = examId,
            studentId = studentId,
            status = parseStatus(status),
            score = score,
            totalPointsEarned = totalPointsEarned,
            totalPointsPossible = totalPointsPossible,
            passed = passed ?: false,
            startedAt = parseDateTime(startedAt),
            completedAt = completedAt?.let { parseDateTime(it) },
            timeSpentSeconds = timeSpentSeconds,
            attemptNumber = attemptNumber,
            answers = emptyList() // Se cargan por separado
        )
    }
    
    /**
     * Convierte String a ExamStatus
     */
    private fun parseStatus(status: String): ExamStatus {
        return when (status.lowercase()) {
            "in_progress" -> ExamStatus.IN_PROGRESS
            "completed" -> ExamStatus.COMPLETED
            "abandoned" -> ExamStatus.ABANDONED
            "timed_out" -> ExamStatus.TIMED_OUT
            else -> ExamStatus.IN_PROGRESS
        }
    }
    
    /**
     * Convierte ExamAttempt a ExamAttemptInsertDto
     */
    fun ExamAttempt.toInsertDto(): ExamAttemptInsertDto {
        return ExamAttemptInsertDto(
            examId = examId,
            studentId = studentId,
            attemptNumber = attemptNumber,
            status = statusToString(status)
        )
    }
    
    /**
     * Convierte ExamStatus a String
     */
    private fun statusToString(status: ExamStatus): String {
        return when (status) {
            ExamStatus.IN_PROGRESS -> "in_progress"
            ExamStatus.COMPLETED -> "completed"
            ExamStatus.ABANDONED -> "abandoned"
            ExamStatus.TIMED_OUT -> "timed_out"
            else -> "in_progress"
        }
    }
    
    /**
     * Convierte QuestionAnswerDto a QuestionAttempt
     */
    fun QuestionAnswerDto.toDomain(): QuestionAttempt {
        return QuestionAttempt(
            id = id,
            attemptId = attemptId,
            questionId = questionId,
            selectedOptionId = selectedOptionId,
            answerText = answerText,
            isCorrect = isCorrect,
            pointsEarned = pointsEarned ?: 0.0,
            timeSpentSeconds = timeSpentSeconds
        )
    }
    
    /**
     * Convierte QuestionAttempt a QuestionAnswerInsertDto
     */
    fun QuestionAttempt.toInsertDto(): QuestionAnswerInsertDto {
        return QuestionAnswerInsertDto(
            attemptId = attemptId,
            questionId = questionId,
            selectedOptionId = selectedOptionId,
            answerText = answerText,
            timeSpentSeconds = timeSpentSeconds
        )
    }
    
    /**
     * Convierte lista de ExamAttemptDto a lista de ExamAttempt
     */
    fun List<ExamAttemptDto>.toDomain(): List<ExamAttempt> {
        return map { it.toDomain() }
    }
    
    /**
     * Convierte lista de QuestionAnswerDto a lista de QuestionAttempt
     */
    fun List<QuestionAnswerDto>.toAnswersDomain(): List<QuestionAttempt> {
        return map { it.toDomain() }
    }
    
    /**
     * Parsea string ISO 8601 a LocalDateTime
     */
    private fun parseDateTime(isoString: String): LocalDateTime {
        return try {
            val instant = Instant.parse(isoString)
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }
}

