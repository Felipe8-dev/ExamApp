package com.example.examapp.presentation.ui.exam

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examapp.presentation.viewmodels.ExamViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamResultScreen(
    onBackToHome: () -> Unit,
    userId: String,
    viewModel: ExamViewModel = hiltViewModel()
) {
    val examResult by viewModel.examResult.collectAsState()
    val examState by viewModel.examState.collectAsState()
    var hasSaved by remember { mutableStateOf(false) }
    
    // Guardar el resultado cuando se muestra la pantalla
    LaunchedEffect(examResult, examState.isCompleted) {
        if (examResult != null && examState.isCompleted && !hasSaved) {
            hasSaved = true
            viewModel.saveExamResult(userId)
        }
    }
    
    if (examResult == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator()
                Text("Cargando resultados...")
            }
        }
        return
    }
    
    val result = examResult!!
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultados del Examen") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Puntuación
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Puntuación",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${String.format("%.2f", result.totalScore)} / ${result.maxScore}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Calificación
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (result.grade) {
                        5 -> MaterialTheme.colorScheme.primaryContainer
                        4 -> MaterialTheme.colorScheme.secondaryContainer
                        3 -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.errorContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Calificación",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${result.grade} / 5",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // Fallos
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Veces que fallaste: ${result.totalFailures}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            
            // Recomendaciones
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Recomendaciones",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    result.recommendations.forEach { recommendation ->
                        Text(
                            text = "• $recommendation",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Botón para volver al inicio
            Button(
                onClick = {
                    viewModel.exitExam()
                    onBackToHome()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al Inicio")
            }
        }
    }
}

