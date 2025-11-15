package com.example.examapp.data.local.dao

import androidx.room.*
import com.example.examapp.data.local.entities.LocalQuestionAttempt
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionAttemptDao {
    @Query("SELECT * FROM question_attempts WHERE examHistoryId = :examHistoryId")
    suspend fun getAttemptsByExamHistory(examHistoryId: Long): List<LocalQuestionAttempt>
    
    @Query("SELECT * FROM question_attempts WHERE examHistoryId = :examHistoryId AND category = :category")
    suspend fun getAttemptsByCategory(examHistoryId: Long, category: String): List<LocalQuestionAttempt>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: LocalQuestionAttempt): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempts(attempts: List<LocalQuestionAttempt>)
    
    @Query("DELETE FROM question_attempts WHERE examHistoryId = :examHistoryId")
    suspend fun deleteAttemptsByExamHistory(examHistoryId: Long)
}

