package com.example.examapp.data.mappers

import com.example.examapp.data.models.ExamAttemptDto
import com.example.examapp.data.models.ExamAttemptInsertDto
import com.example.examapp.data.models.QuestionAnswerDto
import com.example.examapp.data.models.QuestionAnswerInsertDto
import com.example.examapp.domain.entities.ExamAttempt
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
            status = status,
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
     * Convierte ExamAttempt a ExamAttemptInsertDto
     */
    fun ExamAttempt.toInsertDto(): ExamAttemptInsertDto {
        return ExamAttemptInsertDto(
            examId = examId,
            studentId = studentId,
            attemptNumber = attemptNumber,
            status = status
        )
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

