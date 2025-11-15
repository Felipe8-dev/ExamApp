package com.example.examapp.domain.usecases.exam

import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.entities.ExamStatus
import com.example.examapp.domain.repositories.ExamAttemptRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class StartExamUseCase @Inject constructor(
    private val examAttemptRepository: ExamAttemptRepository
) {
    suspend operator fun invoke(examId: String, studentId: String): Result<ExamAttempt> {
        val examAttempt = ExamAttempt(
            id = UUID.randomUUID().toString(),
            examId = examId,
            studentId = studentId,
            status = ExamStatus.IN_PROGRESS,
            startedAt = LocalDateTime.now(),
            attemptNumber = 1
        )
        return examAttemptRepository.createExamAttempt(examAttempt)
    }
}