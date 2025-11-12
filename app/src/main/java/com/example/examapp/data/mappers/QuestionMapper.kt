package com.example.examapp.data.mappers

import com.example.examapp.data.models.QuestionDto
import com.example.examapp.data.models.QuestionInsertDto
import com.example.examapp.data.models.QuestionOptionDto
import com.example.examapp.data.models.QuestionOptionInsertDto
import com.example.examapp.domain.entities.Question
import com.example.examapp.domain.entities.QuestionOption

/**
 * Mapper para convertir entre QuestionDto y Question (entidad de dominio)
 */
object QuestionMapper {
    
    /**
     * Convierte QuestionDto a Question
     */
    fun QuestionDto.toDomain(options: List<QuestionOptionDto> = emptyList()): Question {
        return Question(
            id = id,
            examId = examId,
            questionText = questionText,
            questionType = questionType,
            points = points,
            orderIndex = orderIndex,
            explanation = explanation,
            difficulty = difficulty,
            tags = tags ?: emptyList(),
            options = options.map { it.toDomain() }
        )
    }
    
    /**
     * Convierte Question a QuestionInsertDto
     */
    fun Question.toInsertDto(): QuestionInsertDto {
        return QuestionInsertDto(
            examId = examId,
            questionText = questionText,
            questionType = questionType,
            points = points,
            orderIndex = orderIndex,
            explanation = explanation,
            difficulty = difficulty,
            tags = tags.takeIf { it.isNotEmpty() }
        )
    }
    
    /**
     * Convierte QuestionOptionDto a QuestionOption
     */
    fun QuestionOptionDto.toDomain(): QuestionOption {
        return QuestionOption(
            id = id,
            questionId = questionId,
            optionText = optionText,
            isCorrect = isCorrect,
            orderIndex = orderIndex
        )
    }
    
    /**
     * Convierte QuestionOption a QuestionOptionInsertDto
     */
    fun QuestionOption.toInsertDto(): QuestionOptionInsertDto {
        return QuestionOptionInsertDto(
            questionId = questionId,
            optionText = optionText,
            isCorrect = isCorrect,
            orderIndex = orderIndex
        )
    }
    
    /**
     * Convierte lista de QuestionDto a lista de Question
     */
    fun List<QuestionDto>.toDomain(): List<Question> {
        return map { it.toDomain() }
    }
    
    /**
     * Convierte lista de QuestionOptionDto a lista de QuestionOption
     */
    fun List<QuestionOptionDto>.toOptionsDomain(): List<QuestionOption> {
        return map { it.toDomain() }
    }
}

