package com.example.examapp.domain.repositories

import com.example.examapp.domain.entities.Exam
import com.example.examapp.domain.entities.ExamAttempt

interface ExamRepository {
    suspend fun createExam(exam: Exam): Result<Exam>
    suspend fun getExamById(examId: String): Result<Exam>
    suspend fun getExamByAccessCode(accessCode: String): Result<Exam>
    suspend fun getExamsByProfessor(professorId: String): Result<List<Exam>>
    suspend fun updateExam(exam: Exam): Result<Exam>
    suspend fun deleteExam(examId: String): Result<Unit>
    suspend fun generateAccessCode(): String
}