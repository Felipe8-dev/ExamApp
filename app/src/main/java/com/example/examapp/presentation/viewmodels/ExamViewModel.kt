package com.example.examapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.data.local.entities.LocalQuestion
import com.example.examapp.data.repositories.LocalExamRepository
import com.example.examapp.domain.entities.ExamResult
import com.example.examapp.domain.entities.ExamState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ExamViewModel @Inject constructor(
    private val localExamRepository: LocalExamRepository
) : ViewModel() {
    
    private val _examState = MutableStateFlow(ExamState())
    val examState: StateFlow<ExamState> = _examState.asStateFlow()
    
    private val _examResult = MutableStateFlow<ExamResult?>(null)
    val examResult: StateFlow<ExamResult?> = _examResult.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    fun startExam() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allQuestions = localExamRepository.getAllQuestionsList()
                if (allQuestions.isEmpty()) {
                    // Si no hay preguntas, cargar las iniciales
                    loadInitialQuestions()
                    val questions = localExamRepository.getAllQuestionsList()
                    initializeExam(questions)
                } else {
                    initializeExam(allQuestions)
                }
            } catch (e: Exception) {
                // Error handling
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    private fun initializeExam(questions: List<LocalQuestion>) {
        // Mezclar preguntas y opciones
        val shuffledQuestions = questions.shuffled().map { question ->
            val options = listOf(question.option1, question.option2, question.option3, question.option4)
            val shuffledIndices = (0..3).shuffled()
            val shuffledOptions = shuffledIndices.map { options[it] }
            val newCorrectIndex = shuffledIndices.indexOf(question.correctAnswerIndex)
            
            LocalQuestion(
                id = question.id,
                text = question.text,
                category = question.category,
                correctAnswerIndex = newCorrectIndex,
                option1 = shuffledOptions[0],
                option2 = shuffledOptions[1],
                option3 = shuffledOptions[2],
                option4 = shuffledOptions[3]
            )
        }
        
        _examState.value = ExamState(
            questions = questions,
            shuffledQuestions = shuffledQuestions,
            currentQuestionIndex = 0,
            questionScores = emptyMap(),
            blockedOptions = emptyMap(),
            questionFailures = emptyMap()
        )
    }
    
    fun selectAnswer(optionIndex: Int) {
        val currentState = _examState.value
        val currentQuestion = currentState.shuffledQuestions[currentState.currentQuestionIndex]
        
        if (currentQuestion.correctAnswerIndex == optionIndex) {
            // Respuesta correcta
            val currentScore = currentState.questionScores[currentState.currentQuestionIndex] ?: 1.0
            val updatedScores = currentState.questionScores + (currentState.currentQuestionIndex to currentScore)
            
            // Pasar a la siguiente pregunta
            val nextIndex = currentState.currentQuestionIndex + 1
            if (nextIndex >= currentState.shuffledQuestions.size) {
                // Examen completado
                completeExam(updatedScores, currentState.questionFailures)
            } else {
                _examState.value = currentState.copy(
                    questionScores = updatedScores,
                    currentQuestionIndex = nextIndex
                )
            }
        } else {
            // Respuesta incorrecta
            val currentBlocked = currentState.blockedOptions[currentState.currentQuestionIndex] ?: emptySet()
            val updatedBlocked = currentBlocked + optionIndex
            
            val currentScore = currentState.questionScores[currentState.currentQuestionIndex] ?: 1.0
            val newScore = (currentScore - 0.25).coerceAtLeast(0.25)
            val updatedScores = currentState.questionScores + (currentState.currentQuestionIndex to newScore)
            
            val currentFailures = currentState.questionFailures[currentState.currentQuestionIndex] ?: 0
            val updatedFailures = currentState.questionFailures + (currentState.currentQuestionIndex to (currentFailures + 1))
            
            _examState.value = currentState.copy(
                questionScores = updatedScores,
                blockedOptions = currentState.blockedOptions + (currentState.currentQuestionIndex to updatedBlocked),
                questionFailures = updatedFailures
            )
        }
    }
    
    private fun completeExam(scores: Map<Int, Double>, failures: Map<Int, Int>) {
        val currentState = _examState.value
        
        // Asegurarse de que todas las preguntas tengan un score (incluso las no respondidas)
        val allScores = mutableMapOf<Int, Double>()
        for (i in currentState.shuffledQuestions.indices) {
            allScores[i] = scores[i] ?: 0.0
        }
        
        val totalScore = allScores.values.sum()
        val totalFailures = failures.values.sum()
        val grade = calculateGrade(totalScore)
        
        // Calcular fallos por categoría
        val categoryFailures = mutableMapOf<String, Int>()
        failures.forEach { (questionIndex, failureCount) ->
            val question = currentState.shuffledQuestions[questionIndex]
            categoryFailures[question.category] = (categoryFailures[question.category] ?: 0) + failureCount
        }
        
        val recommendations = generateRecommendations(failures, categoryFailures, currentState.shuffledQuestions)
        
        _examResult.value = ExamResult(
            totalScore = totalScore,
            grade = grade,
            totalFailures = totalFailures,
            categoryFailures = categoryFailures,
            recommendations = recommendations
        )
        
        _examState.value = currentState.copy(
            isCompleted = true,
            totalScore = totalScore,
            totalFailures = totalFailures,
            questionScores = allScores
        )
    }
    
    private fun calculateGrade(score: Double): Int {
        return when {
            score >= 18.0 -> 5
            score >= 15.0 -> 4
            score >= 12.0 -> 3
            score >= 9.0 -> 2
            else -> 1
        }
    }
    
    private fun generateRecommendations(
        failures: Map<Int, Int>,
        categoryFailures: Map<String, Int>,
        questions: List<LocalQuestion>
    ): List<String> {
        val recommendations = mutableListOf<String>()
        
        // Recomendaciones generales
        val totalFailures = failures.values.sum()
        if (totalFailures > 10) {
            recommendations.add("Has tenido muchas dificultades. Te recomendamos repasar los conceptos fundamentales antes de intentar nuevamente.")
        } else if (totalFailures > 5) {
            recommendations.add("Has tenido algunas dificultades. Revisa los temas donde más fallaste.")
        } else if (totalFailures > 0) {
            recommendations.add("Buen trabajo. Revisa los temas donde tuviste dificultades para mejorar.")
        } else {
            recommendations.add("¡Excelente! Has completado el examen sin errores.")
        }
        
        // Recomendaciones por categoría
        categoryFailures.forEach { (category, count) ->
            val categoryName = when (category) {
                "basicos" -> "conceptos básicos de sistemas operativos"
                "modernos" -> "estructura y características de sistemas operativos modernos"
                "moviles" -> "sistemas operativos en dispositivos móviles"
                "seguridad" -> "seguridad en sistemas operativos"
                else -> category
            }
            
            if (count > 0) {
                recommendations.add("Necesitas reforzar tus conocimientos sobre $categoryName. Has fallado $count vez(es) en esta categoría.")
            }
        }
        
        return recommendations
    }
    
    fun saveExamResult(userId: String) {
        viewModelScope.launch {
            try {
                val result = _examResult.value ?: return@launch
                val state = _examState.value
                
                if (!state.isCompleted) return@launch
                
                val examHistory = com.example.examapp.data.local.entities.LocalExamHistory(
                    userId = userId,
                    totalScore = result.totalScore,
                    grade = result.grade,
                    totalFailures = result.totalFailures
                )
                
                val examHistoryId = localExamRepository.insertExamHistory(examHistory)
                
                // Guardar intentos de preguntas
                val attempts = state.shuffledQuestions.mapIndexed { index, question ->
                    com.example.examapp.data.local.entities.LocalQuestionAttempt(
                        examHistoryId = examHistoryId,
                        questionId = question.id,
                        category = question.category,
                        finalScore = state.questionScores[index] ?: 0.0,
                        failures = state.questionFailures[index] ?: 0
                    )
                }
                
                localExamRepository.insertQuestionAttempts(attempts)
            } catch (e: Exception) {
                // Error al guardar, pero no bloqueamos la UI
                e.printStackTrace()
            }
        }
    }
    
    fun resetExam() {
        val currentState = _examState.value
        initializeExam(currentState.questions)
        _examResult.value = null
    }
    
    fun exitExam() {
        _examState.value = ExamState()
        _examResult.value = null
    }
    
    private suspend fun loadInitialQuestions() {
        // Las preguntas se cargan automáticamente por DatabaseInitializer
        // Solo necesitamos recargar desde la base de datos
        val questions = localExamRepository.getAllQuestionsList()
        if (questions.isNotEmpty()) {
            initializeExam(questions)
        }
    }
}

