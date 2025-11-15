package com.example.examapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.examapp.data.local.dao.ExamHistoryDao
import com.example.examapp.data.local.dao.QuestionAttemptDao
import com.example.examapp.data.local.dao.QuestionDao
import com.example.examapp.data.local.entities.LocalExamHistory
import com.example.examapp.data.local.entities.LocalQuestion
import com.example.examapp.data.local.entities.LocalQuestionAttempt

@Database(
    entities = [
        LocalQuestion::class,
        LocalExamHistory::class,
        LocalQuestionAttempt::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ExamDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
    abstract fun examHistoryDao(): ExamHistoryDao
    abstract fun questionAttemptDao(): QuestionAttemptDao
    
    companion object {
        const val DATABASE_NAME = "exam_database"
    }
}

