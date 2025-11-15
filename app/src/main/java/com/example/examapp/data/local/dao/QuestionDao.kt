package com.example.examapp.data.local.dao

import androidx.room.*
import com.example.examapp.data.local.entities.LocalQuestion
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM local_questions")
    fun getAllQuestions(): Flow<List<LocalQuestion>>
    
    @Query("SELECT * FROM local_questions WHERE id = :id")
    suspend fun getQuestionById(id: Long): LocalQuestion?
    
    @Query("SELECT * FROM local_questions WHERE category = :category")
    suspend fun getQuestionsByCategory(category: String): List<LocalQuestion>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: LocalQuestion): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<LocalQuestion>)
    
    @Query("SELECT COUNT(*) FROM local_questions")
    suspend fun getQuestionCount(): Int
}

