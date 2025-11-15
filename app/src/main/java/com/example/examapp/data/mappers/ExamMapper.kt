package com.example.examapp.data.mappers

import com.example.examapp.data.models.ExamDto
import com.example.examapp.data.models.ExamInsertDto
import com.example.examapp.domain.entities.Exam
import com.example.examapp.domain.entities.ExamStatus
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Mapper para convertir entre ExamDto y Exam (entidad de dominio)
 */
object ExamMapper {
    
    /**
     * Convierte ExamDto a Exam
     */
    fun ExamDto.toDomain(): Exam {
        return Exam(
            id = id,
            title = title,
            description = description ?: "",
            professorId = professorId,
            subject = subject,
            durationMinutes = durationMinutes,
            passingScore = passingScore,
            isPublic = isPublic,
            accessCode = accessCode,
            shuffleQuestions = shuffleQuestions,
            showResultsImmediately = showResultsImmediately,
            allowReview = allowReview,
            maxAttempts = maxAttempts,
            availableFrom = availableFrom?.let { parseDateTime(it) },
            availableUntil = availableUntil?.let { parseDateTime(it) },
            isActive = isActive,
            createdAt = parseDateTime(createdAt),
            status = determineStatus(availableFrom, availableUntil, isActive)
        )
    }
    
    /**
     * Convierte Exam a ExamInsertDto
     */
    fun Exam.toInsertDto(): ExamInsertDto {
        return ExamInsertDto(
            title = title,
            description = description?.takeIf { it.isNotEmpty() },
            professorId = professorId,
            subject = subject,
            durationMinutes = durationMinutes,
            passingScore = passingScore,
            isPublic = isPublic,
            accessCode = accessCode,
            shuffleQuestions = shuffleQuestions,
            showResultsImmediately = showResultsImmediately,
            allowReview = allowReview,
            maxAttempts = maxAttempts,
            availableFrom = availableFrom?.let { formatDateTime(it) },
            availableUntil = availableUntil?.let { formatDateTime(it) }
        )
    }
    
    /**
     * Convierte lista de ExamDto a lista de Exam
     */
    fun List<ExamDto>.toDomain(): List<Exam> {
        return map { it.toDomain() }
    }
    
    /**
     * Determina el estado del examen basado en fechas
     */
    private fun determineStatus(
        availableFrom: String?,
        availableUntil: String?,
        isActive: Boolean
    ): ExamStatus {
        if (!isActive) return ExamStatus.INACTIVE
        
        val now = LocalDateTime.now()
        
        val fromDate = availableFrom?.let { parseDateTime(it) }
        val untilDate = availableUntil?.let { parseDateTime(it) }
        
        return when {
            fromDate != null && now.isBefore(fromDate) -> ExamStatus.UPCOMING
            untilDate != null && now.isAfter(untilDate) -> ExamStatus.CLOSED
            else -> ExamStatus.ACTIVE
        }
    }
    
    /**
     * Convierte LocalDateTime a String ISO 8601
     * Maneja tanto valores nullable como no-nullable
     */
    private fun formatDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.let {
            val instant = it.atZone(ZoneId.systemDefault()).toInstant()
            instant.toString()
        }
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

