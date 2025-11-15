package com.example.examapp.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examapp.data.local.entities.LocalExamHistory
import com.example.examapp.presentation.viewmodels.ExamHistoryViewModel

@Composable
fun ExamHistoryList(
    userId: String,
    viewModel: ExamHistoryViewModel = hiltViewModel()
) {
    val examHistory by viewModel.getExamHistoryByUser(userId).collectAsState(initial = emptyList())
    
    if (examHistory.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay exámenes completados aún",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        examHistory.forEach { history ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Puntuación: ${String.format("%.2f", history.totalScore)} / ${history.maxScore}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Calificación: ${history.grade} / 5",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Fallos: ${history.totalFailures}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
