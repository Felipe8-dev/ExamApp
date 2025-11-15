package com.example.examapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.examapp.presentation.ui.auth.LoginScreen
import com.example.examapp.presentation.ui.auth.RegisterScreen
import com.example.examapp.presentation.ui.exam.ExamResultScreen
import com.example.examapp.presentation.ui.exam.ExamScreen
import com.example.examapp.presentation.ui.home.HomeScreen
import com.example.examapp.presentation.viewmodels.AuthViewModel
import com.example.examapp.ui.theme.ExamAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExamAppTheme {
                ExamAppNavigation()
            }
        }
    }
}

@Composable
fun ExamAppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val uiState by authViewModel.uiState.collectAsState()
    
    // Estado para controlar si ya se verificó la sesión inicial
    var hasCheckedInitialAuth by remember { mutableStateOf(false) }
    
    // Navegar automáticamente cuando se complete la verificación inicial
    LaunchedEffect(uiState.isLoading, uiState.isAuthenticated, uiState.user) {
        if (!uiState.isLoading && !hasCheckedInitialAuth) {
            hasCheckedInitialAuth = true
            // Navegar según el estado de autenticación
            if (uiState.isAuthenticated && uiState.user != null) {
                // Si hay sesión guardada, navegar a home
                navController.navigate("home") {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                // Si no hay sesión, navegar a login
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }
    
    // Navegar automáticamente si el estado de autenticación cambia después de la verificación inicial
    LaunchedEffect(uiState.isAuthenticated, uiState.user) {
        if (hasCheckedInitialAuth && !uiState.isLoading) {
            val currentRoute = navController.currentDestination?.route
            if (uiState.isAuthenticated && uiState.user != null) {
                // Si está autenticado, navegar a home
                if (currentRoute != "home") {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            } else if (!uiState.isAuthenticated) {
                // Si no está autenticado, navegar a login
                if (currentRoute != "login" && currentRoute != "register") {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "splash", // Pantalla de carga inicial
        modifier = Modifier.fillMaxSize()
    ) {
        // Pantalla de carga inicial mientras se verifica la sesión
        composable("splash") {
            SplashScreen()
        }
        
        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = {
                    navController.navigate("home") {
                        // Limpiar el back stack para que no pueda volver al login
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Home (después de autenticarse)
        composable("home") {
            HomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onStartExam = {
                    navController.navigate("exam")
                }
            )
        }
        
        // Pantalla de Examen
        composable("exam") {
            ExamScreen(
                onExit = {
                    navController.navigate("home") {
                        popUpTo("exam") { inclusive = true }
                    }
                },
                onComplete = {
                    navController.navigate("exam_result") {
                        popUpTo("exam") { inclusive = true }
                    }
                }
            )
        }
        
        // Pantalla de Resultados
        composable("exam_result") {
            val currentUser = uiState.user
            if (currentUser != null) {
                ExamResultScreen(
                    onBackToHome = {
                        navController.navigate("home") {
                            popUpTo("exam_result") { inclusive = true }
                        }
                    },
                    userId = currentUser.id
                )
            } else {
                // Si no hay usuario, volver al home
                LaunchedEffect(Unit) {
                    navController.navigate("home") {
                        popUpTo("exam_result") { inclusive = true }
                    }
                }
            }
        }
    }
}

/**
 * Pantalla de carga inicial mientras se verifica la sesión
 */
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Verificando sesión...")
        }
    }
}