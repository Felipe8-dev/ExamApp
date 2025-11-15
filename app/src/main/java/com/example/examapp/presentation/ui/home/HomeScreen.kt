package com.example.examapp.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.examapp.domain.entities.UserType
import com.example.examapp.presentation.ui.home.ExamHistoryList
import com.example.examapp.presentation.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onStartExam: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    
    // Estado del drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Intentar obtener el usuario cuando se monta la pantalla
    // Esto asegura que siempre tengamos el usuario actualizado
    var hasTriedLoad by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        if (!hasTriedLoad) {
            hasTriedLoad = true
            // Si está autenticado pero no hay usuario, intentar obtenerlo
            if (uiState.isAuthenticated && user == null && !uiState.isLoading) {
                viewModel.refreshUser()
            }
        }
    }
    
    // Observar cambios en el estado de autenticación y navegar cuando se cierre sesión
    LaunchedEffect(uiState.isAuthenticated) {
        if (!uiState.isAuthenticated && !uiState.isLoading) {
            // Si no está autenticado y no está cargando, navegar a login
            onLogout()
        }
    }

    // Obtener el ancho de la pantalla para calcular la mitad
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val drawerWidth = screenWidth * 0.6f // Mitad de la pantalla
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Contenido del drawer con información del usuario
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(drawerWidth)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            ) {
                UserInfoDrawerContent(
                    user = user,
                    isLoading = uiState.isLoading,
                    onLogout = {
                        viewModel.logout()
                    },
                    onDismiss = {
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
        scrimColor = Color.Black.copy(alpha = 0.6f) // Fondo oscuro semitransparente detrás del drawer
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("ExamApp - Inicio") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (user != null) {
                    // Panel según rol
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = when (user.userType) {
                                UserType.PROFESSOR -> MaterialTheme.colorScheme.primaryContainer
                                UserType.STUDENT -> MaterialTheme.colorScheme.secondaryContainer
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = when (user.userType) {
                                    UserType.PROFESSOR -> "🎓 Panel de Profesor"
                                    UserType.STUDENT -> "📚 Panel de Estudiante"
                                },
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = when (user.userType) {
                                    UserType.PROFESSOR -> 
                                        "Aquí podrás crear y gestionar tus exámenes, " +
                                        "añadir preguntas y revisar los resultados de tus estudiantes."
                                    UserType.STUDENT -> 
                                        "Aquí podrás ver los exámenes disponibles, " +
                                        "realizar tus evaluaciones y revisar tus resultados."
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            // Botón para iniciar examen (solo para estudiantes)
                            if (user.userType == UserType.STUDENT) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onStartExam,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Iniciar Examen")
                                }
                            }
                        }
                    }

                    // Sección de Historial
                    Text(
                        text = "Historial",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Historial de exámenes
                    ExamHistoryList(userId = user.id)
                } else {
                    // Estado de carga o error
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Cargando información...")
                            }
                        }
                    } else {
                        // Si no está cargando y no hay usuario, mostrar error o mensaje
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (uiState.error != null) {
                                Text(
                                    text = uiState.error ?: "Error desconocido",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            } else {
                                Text(
                                    text = "No se pudo cargar la información del usuario",
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                            Button(
                                onClick = { viewModel.refreshUser() },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Contenido del drawer con la información del usuario
 */
@Composable
fun UserInfoDrawerContent(
    user: com.example.examapp.domain.entities.User?,
    isLoading: Boolean,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Espaciado superior para bajar el contenido
        Spacer(modifier = Modifier.height(52.dp))
        
        // Título del drawer
        Text(
            text = "Información del Usuario",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        if (user != null) {
            // Nombre
            Column {
                Text(
                    text = "Nombre",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            // Email
            Column {
                Text(
                    text = "Email",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            HorizontalDivider(color = Color.White.copy(alpha = 0.3f))

            // Rol
            Column {
                Text(
                    text = "Rol",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = when (user.userType) {
                        UserType.PROFESSOR -> "Profesor"
                        UserType.STUDENT -> "Estudiante"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón de cerrar sesión
            Button(
                onClick = {
                    onLogout()
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onError
                    )
                } else {
                    Text("Cerrar Sesión")
                }
            }
        } else if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Text(
                text = "No se pudo cargar la información del usuario",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

