package com.example.examapp.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "question_attempts",
    foreignKeys = [
        ForeignKey(
            entity = LocalExamHistory::class,
            parentColumns = ["id"],
            childColumns = ["examHistoryId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalQuestion::class,
            parentColumns = ["id"],
            childColumns = ["questionId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LocalQuestionAttempt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val examHistoryId: Long,
    val questionId: Long,
    val category: String,
    val finalScore: Double, // Puntuación final de la pregunta (puede ser 0.25, 0.5, 0.75, 1.0)
    val failures: Int // Cantidad de veces que falló en esta pregunta
)

