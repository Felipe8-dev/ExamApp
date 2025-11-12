package com.example.examapp.domain.usecases.exam

import com.example.examapp.domain.entities.AnswerHistory
import com.example.examapp.domain.entities.QuestionAttempt
import com.example.examapp.domain.repositories.ExamAttemptRepository
import javax.inject.Inject
import java.util.UUID

class SubmitAnswerUseCase @Inject constructor(
    private val examAttemptRepository: ExamAttemptRepository
) {
    suspend operator fun invoke(
        questionAttemptId: String,
        selectedOptionId: String,
        isCorrect: Boolean
    ): Result<AnswerHistory> {
        val answerHistory = AnswerHistory(
            id = UUID.randomUUID().toString(),
            questionAttemptId = questionAttemptId,
            selectedOptionId = selectedOptionId,
            isCorrect = isCorrect,
            answeredAt = System.currentTimeMillis().toString()
        )
        return examAttemptRepository.saveAnswerHistory(answerHistory)
    }
}