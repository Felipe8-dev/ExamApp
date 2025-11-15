package com.example.examapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.examapp.domain.entities.User
import com.example.examapp.domain.usecases.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    private var hasCheckedAuth = false

    init {
        // Verificar el estado de autenticación al inicializar el ViewModel
        // Esto se ejecuta cada vez que se crea una nueva instancia (al iniciar la app)
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        // Solo verificar una vez por instancia para evitar bucles infinitos
        if (hasCheckedAuth) return
        
        viewModelScope.launch {
            hasCheckedAuth = true
            // Establecer estado de carga mientras se verifica
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val isAuth = isAuthenticatedUseCase()
            if (isAuth) {
                // Si hay sesión guardada, obtener el usuario
                getCurrentUserUseCase()
                    .take(1)
                    .catch { e ->
                        // Manejar excepciones del Flow correctamente sin emitir
                        _uiState.value = _uiState.value.copy(
                            user = null,
                            isAuthenticated = false,
                            isLoading = false
                        )
                    }
                    .collect { result ->
                        result.onSuccess { user ->
                            _uiState.value = _uiState.value.copy(
                                user = user,
                                isAuthenticated = user != null,
                                isLoading = false
                            )
                        }.onFailure {
                            // Si falla al obtener el usuario, no está autenticado
                            _uiState.value = _uiState.value.copy(
                                user = null,
                                isAuthenticated = false,
                                isLoading = false
                            )
                        }
                        // No usar return@collect, dejar que take(1) maneje la cancelación
                    }
            } else {
                // No hay sesión guardada
                _uiState.value = _uiState.value.copy(
                    user = null,
                    isAuthenticated = false,
                    isLoading = false
                )
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            loginUseCase(email, password).collect { result ->
                result.onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        isAuthenticated = true,
                        error = null
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al iniciar sesión"
                    )
                }
            }
        }
    }

    fun register(email: String, password: String, fullName: String, isProfessor: Boolean) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            registerUseCase(email, password, fullName, isProfessor).collect { result ->
                result.onSuccess { user ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = user,
                        isAuthenticated = true,
                        error = null
                    )
                }.onFailure { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al registrar"
                    )
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Usar take(1) en lugar de first() para evitar problemas con AbortFlowException
                logoutUseCase()
                    .take(1)
                    .catch { e ->
                        // Manejar excepciones del Flow correctamente
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Error al cerrar sesión: ${e.message}"
                        )
                    }
                    .collect { result ->
                        result.onSuccess {
                            // Resetear el estado completamente
                            hasCheckedAuth = false // Resetear para permitir verificación futura
                            _uiState.value = AuthUiState(
                                isLoading = false,
                                user = null,
                                isAuthenticated = false,
                                error = null
                            )
                        }.onFailure { exception ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = exception.message ?: "Error al cerrar sesión"
                            )
                        }
                    }
            } catch (e: Exception) {
                // Si hay un error, al menos resetear el estado local
                hasCheckedAuth = false
                _uiState.value = AuthUiState(
                    isLoading = false,
                    user = null,
                    isAuthenticated = false,
                    error = "Error al cerrar sesión: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Refresca el usuario actual desde el servidor
     * Útil cuando navegas a HomeScreen y necesitas obtener el usuario
     */
    fun refreshUser() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val isAuth = isAuthenticatedUseCase()
                if (isAuth) {
                    // Usar take(1) y catch() para manejar excepciones del Flow correctamente
                    // No usar return@collect, dejar que take(1) maneje la cancelación automáticamente
                    getCurrentUserUseCase()
                        .take(1)
                        .catch { e ->
                            // Manejar excepciones del Flow correctamente sin emitir
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                user = null,
                                isAuthenticated = false,
                                error = "Error al obtener información del usuario: ${e.message}"
                            )
                        }
                        .collect { result ->
                            result.onSuccess { user ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    user = user,
                                    isAuthenticated = user != null,
                                    error = null
                                )
                            }.onFailure { exception ->
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    user = null,
                                    isAuthenticated = false,
                                    error = "Error al obtener información del usuario: ${exception.message}"
                                )
                            }
                            // take(1) cancelará automáticamente después del primer valor
                        }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        user = null,
                        isAuthenticated = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)