package com.example.examapp.domain.usecases.student

import com.example.examapp.domain.entities.Exam
import com.example.examapp.domain.repositories.ExamRepository
import javax.inject.Inject

class JoinExamUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(accessCode: String): Result<Exam> {
        return examRepository.getExamByAccessCode(accessCode)
    }
}