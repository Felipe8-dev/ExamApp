package com.example.examapp.data.repositories

import com.example.examapp.data.local.dao.ExamHistoryDao
import com.example.examapp.data.local.dao.QuestionAttemptDao
import com.example.examapp.data.local.dao.QuestionDao
import com.example.examapp.data.local.entities.LocalExamHistory
import com.example.examapp.data.local.entities.LocalQuestion
import com.example.examapp.data.local.entities.LocalQuestionAttempt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalExamRepository @Inject constructor(
    private val questionDao: QuestionDao,
    private val examHistoryDao: ExamHistoryDao,
    private val questionAttemptDao: QuestionAttemptDao
) {
    fun getAllQuestions(): Flow<List<LocalQuestion>> {
        return questionDao.getAllQuestions()
    }
    
    suspend fun getAllQuestionsList(): List<LocalQuestion> {
        return questionDao.getAllQuestions().first()
    }
    
    suspend fun getQuestionsByCategory(category: String): List<LocalQuestion> {
        return questionDao.getQuestionsByCategory(category)
    }
    
    suspend fun insertQuestions(questions: List<LocalQuestion>) {
        questionDao.insertQuestions(questions)
    }
    
    suspend fun getQuestionCount(): Int {
        return questionDao.getQuestionCount()
    }
    
    fun getExamHistoryByUser(userId: String): Flow<List<LocalExamHistory>> {
        return examHistoryDao.getExamHistoryByUser(userId)
    }
    
    suspend fun insertExamHistory(examHistory: LocalExamHistory): Long {
        return examHistoryDao.insertExamHistory(examHistory)
    }
    
    suspend fun insertQuestionAttempts(attempts: List<LocalQuestionAttempt>) {
        questionAttemptDao.insertAttempts(attempts)
    }
    
    suspend fun getAttemptsByExamHistory(examHistoryId: Long): List<LocalQuestionAttempt> {
        return questionAttemptDao.getAttemptsByExamHistory(examHistoryId)
    }
    
    suspend fun getAttemptsByCategory(examHistoryId: Long, category: String): List<LocalQuestionAttempt> {
        return questionAttemptDao.getAttemptsByCategory(examHistoryId, category)
    }
}

