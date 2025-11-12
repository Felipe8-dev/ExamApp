package com.example.examapp.domain.repositories

import com.example.examapp.domain.entities.ExamAttempt
import com.example.examapp.domain.entities.QuestionAttempt
import com.example.examapp.domain.entities.AnswerHistory

interface ExamAttemptRepository {
    suspend fun createExamAttempt(examAttempt: ExamAttempt): Result<ExamAttempt>
    suspend fun updateExamAttempt(examAttempt: ExamAttempt): Result<ExamAttempt>
    suspend fun getExamAttempt(attemptId: String): Result<ExamAttempt>
    suspend fun getExamAttemptsByStudent(studentId: String): Result<List<ExamAttempt>>
    suspend fun saveQuestionAttempt(questionAttempt: QuestionAttempt): Result<QuestionAttempt>
    suspend fun saveAnswerHistory(answerHistory: AnswerHistory): Result<AnswerHistory>
    suspend fun getQuestionAttempts(examAttemptId: String): Result<List<QuestionAttempt>>
}