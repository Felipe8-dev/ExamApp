package com.example.examapp.domain.usecases.exam

import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.entities.ExamStatus
import com.example.examapp.domain.repositories.ExamAttemptRepository
import javax.inject.Inject
import java.util.UUID

class StartExamUseCase @Inject constructor(
    private val examAttemptRepository: ExamAttemptRepository
) {
    suspend operator fun invoke(examId: String, studentId: String): Result<ExamAttempt> {
        val examAttempt = ExamAttempt(
            id = UUID.randomUUID().toString(),
            examId = examId,
            studentId = studentId,
            status = ExamStatus.IN_PROGRESS,
            startedAt = System.currentTimeMillis().toString()
        )
        return examAttemptRepository.createExamAttempt(examAttempt)
    }
}