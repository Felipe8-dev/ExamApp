package com.example.examapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "exam_history")
data class LocalExamHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val totalScore: Double,
    val maxScore: Double = 20.0,
    val grade: Int, // Calificación del 1 al 5
    val totalFailures: Int, // Total de veces que falló
    val completedAt: Long = System.currentTimeMillis()
)

