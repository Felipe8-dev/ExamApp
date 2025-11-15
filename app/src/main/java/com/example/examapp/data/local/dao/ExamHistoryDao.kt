package com.example.examapp.data.local.dao

import androidx.room.*
import com.example.examapp.data.local.entities.LocalExamHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamHistoryDao {
    @Query("SELECT * FROM exam_history WHERE userId = :userId ORDER BY completedAt DESC")
    fun getExamHistoryByUser(userId: String): Flow<List<LocalExamHistory>>
    
    @Query("SELECT * FROM exam_history WHERE id = :id")
    suspend fun getExamHistoryById(id: Long): LocalExamHistory?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExamHistory(examHistory: LocalExamHistory): Long
    
    @Delete
    suspend fun deleteExamHistory(examHistory: LocalExamHistory)
    
    @Query("DELETE FROM exam_history WHERE userId = :userId")
    suspend fun deleteAllHistoryForUser(userId: String)
}

