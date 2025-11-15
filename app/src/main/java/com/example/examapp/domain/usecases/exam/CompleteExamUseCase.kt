package com.example.examapp.domain.usecases.exam

import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.entities.ExamStatus
import com.example.examapp.domain.repositories.ExamAttemptRepository
import com.example.examapp.domain.repositories.AIRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CompleteExamUseCase @Inject constructor(
    private val examAttemptRepository: ExamAttemptRepository,
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(examAttempt: ExamAttempt, finalScore: Double): Result<ExamAttempt> {
        val completedAttempt = examAttempt.copy(
            status = ExamStatus.COMPLETED,
            score = finalScore,
            completedAt = LocalDateTime.now()
        )
        return examAttemptRepository.updateExamAttempt(completedAttempt)
    }
}