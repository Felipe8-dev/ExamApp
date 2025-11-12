package com.example.examapp.domain.usecases.student

import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.repositories.ExamAttemptRepository
import javax.inject.Inject

class GetStudentExamHistoryUseCase @Inject constructor(
    private val examAttemptRepository: ExamAttemptRepository
) {
    suspend operator fun invoke(studentId: String): Result<List<ExamAttempt>> {
        return examAttemptRepository.getExamAttemptsByStudent(studentId)
    }
}