package com.example.examapp.domain.usecases.professor

import com.example.examapp.domain.entities.Exam
import com.example.examapp.domain.repositories.ExamRepository
import javax.inject.Inject

class CreateExamUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(exam: Exam): Result<Exam> {
        val examWithAccessCode = exam.copy(
            accessCode = examRepository.generateAccessCode()
        )
        return examRepository.createExam(examWithAccessCode)
    }
}