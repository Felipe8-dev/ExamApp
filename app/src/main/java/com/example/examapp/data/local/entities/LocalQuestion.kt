package com.example.examapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_questions")
data class LocalQuestion(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val category: String, // "basicos", "modernos", "moviles", "seguridad"
    val correctAnswerIndex: Int, // Índice de la respuesta correcta (0-3)
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String
)

