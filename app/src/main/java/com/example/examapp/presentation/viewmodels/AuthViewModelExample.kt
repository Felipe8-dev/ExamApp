package com.example.examapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.domain.entities.User
import com.example.examapp.domain.usecases.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel de ejemplo para autenticación
 * Muestra cómo usar los casos de uso de autenticación
 */
@HiltViewModel
class AuthViewModelExample @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signInWithGitHubUseCase: SignInWithGitHubUseCase,
    private val signInWithFacebookUseCase: SignInWithFacebookUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    
    // ==========================================
    // ESTADO DE LA UI
    // ==========================================
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    // ==========================================
    // ESTADOS DE AUTENTICACIÓN
    // ==========================================
    
    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        data class Error(val message: String) : AuthState()
        data class Success(val message: String) : AuthState()
    }
    
    // ==========================================
    // INIT
    // ==========================================
    
    init {
        checkAuthStatus()
    }
    
    // ==========================================
    // MÉTODOS PÚBLICOS
    // ==========================================
    
    /**
     * Verifica el estado de autenticación al iniciar
     */
    fun checkAuthStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val isAuth = isAuthenticatedUseCase()
                
                if (isAuth) {
                    getCurrentUserUseCase().collect { result ->
                        result.onSuccess { user ->
                            _currentUser.value = user
                            _authState.value = AuthState.Authenticated
                        }.onFailure {
                            _authState.value = AuthState.Unauthenticated
                        }
                    }
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error desconocido")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Registra un nuevo usuario
     */
    fun register(
        email: String,
        password: String,
        fullName: String,
        isProfessor: Boolean
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                registerUseCase(
                    email = email.trim(),
                    password = password,
                    fullName = fullName.trim(),
                    isProfessor = isProfessor
                ).collect { result ->
                    result.onSuccess { user ->
                        _currentUser.value = user
                        _authState.value = AuthState.Success("¡Registro exitoso!")
                        // Cambiar a Authenticated después de un momento
                        kotlinx.coroutines.delay(1000)
                        _authState.value = AuthState.Authenticated
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error al registrar")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Inicia sesión con email y contraseña
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                loginUseCase(
                    email = email.trim(),
                    password = password
                ).collect { result ->
                    result.onSuccess { user ->
                        _currentUser.value = user
                        _authState.value = AuthState.Success("¡Bienvenido ${user.fullName}!")
                        // Cambiar a Authenticated después de un momento
                        kotlinx.coroutines.delay(1000)
                        _authState.value = AuthState.Authenticated
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error al iniciar sesión")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Inicia sesión con Google
     */
    fun signInWithGoogle() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            
            try {
                signInWithGoogleUseCase().collect { result ->
                    result.onSuccess {
                        // Después del OAuth, obtener el usuario
                        getCurrentUser()
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error con Google")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Inicia sesión con GitHub
     */
    fun signInWithGitHub() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            
            try {
                signInWithGitHubUseCase().collect { result ->
                    result.onSuccess {
                        getCurrentUser()
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error con GitHub")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Inicia sesión con Facebook
     */
    fun signInWithFacebook() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            _isLoading.value = true
            
            try {
                signInWithFacebookUseCase().collect { result ->
                    result.onSuccess {
                        getCurrentUser()
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error con Facebook")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                logoutUseCase().collect { result ->
                    result.onSuccess {
                        _currentUser.value = null
                        _authState.value = AuthState.Unauthenticated
                    }.onFailure { error ->
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Recupera la contraseña
     */
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                resetPasswordUseCase(email.trim()).collect { result ->
                    result.onSuccess {
                        _authState.value = AuthState.Success("Email de recuperación enviado")
                    }.onFailure { error ->
                        _authState.value = AuthState.Error(error.message ?: "Error al enviar email")
                        _errorMessage.value = error.message
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Error inesperado")
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Obtiene el usuario actual
     */
    private fun getCurrentUser() {
        viewModelScope.launch {
            try {
                getCurrentUserUseCase().collect { result ->
                    result.onSuccess { user ->
                        _currentUser.value = user
                        if (user != null) {
                            _authState.value = AuthState.Authenticated
                        }
                    }.onFailure {
                        _authState.value = AuthState.Unauthenticated
                    }
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
    
    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Resetea el estado de autenticación a inicial
     */
    fun resetAuthState() {
        _authState.value = AuthState.Initial
    }
}

/**
 * EJEMPLO DE USO EN COMPOSE:
 * 
 * @Composable
 * fun LoginScreen(viewModel: AuthViewModelExample = hiltViewModel()) {
 *     val authState by viewModel.authState.collectAsState()
 *     val isLoading by viewModel.isLoading.collectAsState()
 *     val errorMessage by viewModel.errorMessage.collectAsState()
 *     
 *     var email by remember { mutableStateOf("") }
 *     var password by remember { mutableStateOf("") }
 *     
 *     LaunchedEffect(authState) {
 *         when (authState) {
 *             is AuthViewModelExample.AuthState.Authenticated -> {
 *                 // Navegar a la pantalla principal
 *             }
 *             is AuthViewModelExample.AuthState.Error -> {
 *                 // Mostrar error
 *             }
 *             else -> {}
 *         }
 *     }
 *     
 *     Column(
 *         modifier = Modifier.fillMaxSize().padding(16.dp),
 *         verticalArrangement = Arrangement.Center
 *     ) {
 *         OutlinedTextField(
 *             value = email,
 *             onValueChange = { email = it },
 *             label = { Text("Email") },
 *             modifier = Modifier.fillMaxWidth()
 *         )
 *         
 *         Spacer(modifier = Modifier.height(8.dp))
 *         
 *         OutlinedTextField(
 *             value = password,
 *             onValueChange = { password = it },
 *             label = { Text("Contraseña") },
 *             visualTransformation = PasswordVisualTransformation(),
 *             modifier = Modifier.fillMaxWidth()
 *         )
 *         
 *         Spacer(modifier = Modifier.height(16.dp))
 *         
 *         Button(
 *             onClick = { viewModel.login(email, password) },
 *             enabled = !isLoading,
 *             modifier = Modifier.fillMaxWidth()
 *         ) {
 *             if (isLoading) {
 *                 CircularProgressIndicator(
 *                     modifier = Modifier.size(24.dp),
 *                     color = Color.White
 *                 )
 *             } else {
 *                 Text("Iniciar Sesión")
 *             }
 *         }
 *         
 *         Spacer(modifier = Modifier.height(16.dp))
 *         
 *         // Botones OAuth
 *         Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
 *             IconButton(onClick = { viewModel.signInWithGoogle() }) {
 *                 Icon(Icons.Default.Google, "Google")
 *             }
 *             IconButton(onClick = { viewModel.signInWithGitHub() }) {
 *                 Icon(Icons.Default.GitHub, "GitHub")
 *             }
 *             IconButton(onClick = { viewModel.signInWithFacebook() }) {
 *                 Icon(Icons.Default.Facebook, "Facebook")
 *             }
 *         }
 *         
 *         errorMessage?.let { error ->
 *             Spacer(modifier = Modifier.height(8.dp))
 *             Text(
 *                 text = error,
 *                 color = MaterialTheme.colorScheme.error,
 *                 style = MaterialTheme.typography.bodySmall
 *             )
 *         }
 *     }
 * }
 */

