package com.example.examapp.presentation.ui.exam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examapp.presentation.viewmodels.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    onExit: () -> Unit,
    onComplete: () -> Unit,
    viewModel: ExamViewModel = hiltViewModel()
) {
    val examState by viewModel.examState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(Unit) {
        if (examState.shuffledQuestions.isEmpty() && !isLoading) {
            viewModel.startExam()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Examen de Sistemas Operativos") },
                actions = {
                    TextButton(onClick = { viewModel.exitExam(); onExit() }) {
                        Text("Salir")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { viewModel.resetExam() }) {
                        Text("Reiniciar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (examState.shuffledQuestions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay preguntas disponibles")
            }
        } else {
            val currentQuestion = examState.shuffledQuestions[examState.currentQuestionIndex]
            val blockedOptions = examState.blockedOptions[examState.currentQuestionIndex] ?: emptySet()
            val questionNumber = examState.currentQuestionIndex + 1
            val totalQuestions = examState.shuffledQuestions.size
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Indicador de progreso
                LinearProgressIndicator(
                    progress = questionNumber.toFloat() / totalQuestions.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "Pregunta $questionNumber de $totalQuestions",
                    style = MaterialTheme.typography.bodyMedium
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Pregunta
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = currentQuestion.text,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Opciones de respuesta
                val options = listOf(
                    currentQuestion.option1,
                    currentQuestion.option2,
                    currentQuestion.option3,
                    currentQuestion.option4
                )
                
                options.forEachIndexed { index, option ->
                    val isBlocked = blockedOptions.contains(index)
                    
                    Button(
                        onClick = { viewModel.selectAnswer(index) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isBlocked,
                        colors = if (isBlocked) {
                            ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        } else {
                            ButtonDefaults.buttonColors()
                        }
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.padding(8.dp)
                        )
                        if (isBlocked) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("(Bloqueada)")
                        }
                    }
                }
            }
        }
    }
    
    // Observar cuando se complete el examen
    LaunchedEffect(examState.isCompleted) {
        if (examState.isCompleted) {
            onComplete()
        }
    }
}

