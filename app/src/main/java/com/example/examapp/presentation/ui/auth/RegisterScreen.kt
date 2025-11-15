package com.example.examapp.presentation.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examapp.presentation.viewmodels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var isProfessor by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    // Navegar cuando el registro sea exitoso
    // Usamos una clave para evitar que se ejecute múltiples veces
    var hasNavigated by remember { mutableStateOf(false) }
    
    LaunchedEffect(uiState.isAuthenticated, uiState.user) {
        if (uiState.isAuthenticated && uiState.user != null && !hasNavigated && !uiState.isLoading) {
            hasNavigated = true
            onRegisterSuccess()
        }
    }
    
    // Resetear la bandera cuando el estado cambia a no autenticado
    LaunchedEffect(uiState.isAuthenticated) {
        if (!uiState.isAuthenticated) {
            hasNavigated = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ExamApp - Registro") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Campo Nombre
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Nombre Completo") },
                placeholder = { Text("Juan Pérez") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("tu@email.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("Mínimo 6 caracteres") },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible)
                                "Ocultar contraseña"
                            else
                                "Mostrar contraseña"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de Rol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Soy profesor", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = isProfessor,
                    onCheckedChange = { isProfessor = it },
                    enabled = !uiState.isLoading
                )
            }

            if (isProfessor) {
                Text(
                    text = "Como profesor podrás crear y gestionar exámenes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            } else {
                Text(
                    text = "Como estudiante podrás realizar exámenes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de error
            if (uiState.error != null) {
                Text(
                    text = uiState.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Registrarse
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank() && fullName.isNotBlank()) {
                        viewModel.register(
                            email = email.trim(),
                            password = password,
                            fullName = fullName.trim(),
                            isProfessor = isProfessor
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading && 
                         email.isNotBlank() && 
                         password.isNotBlank() && 
                         fullName.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (uiState.isLoading) "Registrando..." else "Registrarse")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Login
            TextButton(
                onClick = onNavigateToLogin,
                enabled = !uiState.isLoading
            ) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }
        }
    }
}

