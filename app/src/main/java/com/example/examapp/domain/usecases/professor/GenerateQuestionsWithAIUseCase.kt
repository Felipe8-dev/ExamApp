package com.example.examapp.domain.usecases.professor

import com.example.examapp.domain.entities.Question
import com.example.examapp.domain.repositories.AIRepository
import javax.inject.Inject

class GenerateQuestionsWithAIUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(
        prompt: String,
        numberOfQuestions: Int,
        topics: List<String>
    ): Result<List<Question>> {
        return aiRepository.generateQuestions(prompt, numberOfQuestions, topics)
    }
}