package com.example.examapp.domain.usecases.professor

import com.example.examapp.domain.entities.Exam
import com.example.examapp.domain.repositories.ExamRepository
import javax.inject.Inject

class GetProfessorExamsUseCase @Inject constructor(
    private val examRepository: ExamRepository
) {
    suspend operator fun invoke(professorId: String): Result<List<Exam>> {
        return examRepository.getExamsByProfessor(professorId)
    }
}