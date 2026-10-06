package com.example.loginapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.loginapp.data.UserPreferencesRepository
import com.example.loginapp.model.UsuarioErrores
import com.example.loginapp.model.UsuarioUiState
import com.example.loginapp.navigation.NavigationEvent
import com.example.loginapp.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "LOGIN_APP_LOG"
    private val repository = UserPreferencesRepository(application)

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _errores = MutableStateFlow(UsuarioErrores())
    val errores: StateFlow<UsuarioErrores> = _errores.asStateFlow()

    // Estados independientes para el formulario de Login
    private val _loginCorreo = MutableStateFlow("")
    val loginCorreo: StateFlow<String> = _loginCorreo.asStateFlow()

    private val _loginClave = MutableStateFlow("")
    val loginClave: StateFlow<String> = _loginClave.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvents: SharedFlow<NavigationEvent> = _navigationEvents.asSharedFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.savedNombre,
                repository.savedCorreo,
                repository.savedClave,
                repository.savedDireccion,
                repository.savedEstudiante
            ) { nombre, correo, clave, direccion, estudiante ->
                UsuarioUiState(
                    nombre = nombre,
                    correo = correo,
                    clave = clave,
                    direccion = direccion,
                    esEstudiante = estudiante
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    // --- Controles de Registro ---
    fun onNombreChange(v: String) { _uiState.update { it.copy(nombre = v) } }
    fun onCorreoChange(v: String) { _uiState.update { it.copy(correo = v) } }
    fun onClaveChange(v: String) { _uiState.update { it.copy(clave = v) } }
    fun onDireccionChange(v: String) { _uiState.update { it.copy(direccion = v) } }
    fun onEstudianteChange(v: Boolean) { _uiState.update { it.copy(esEstudiante = v) } }

    // --- Controles de Login ---
    fun onLoginCorreoChange(v: String) {
        _loginCorreo.value = v
        _loginError.value = null
    }

    fun onLoginClaveChange(v: String) {
        _loginClave.value = v
        _loginError.value = null
    }

    fun iniciarSesion() {
        val correoInput = _loginCorreo.value.trim()
        val claveInput = _loginClave.value

        val correoRegistrado = _uiState.value.correo
        val claveRegistrada = _uiState.value.clave

        if (correoInput.isBlank() || claveInput.isBlank()) {
            _loginError.value = "Por favor ingresa correo y contraseña"
            return
        }

        // Validar credenciales contra DataStore
        if (correoInput.equals(correoRegistrado, ignoreCase = true) && claveInput == claveRegistrada) {
            _loginError.value = null
            viewModelScope.launch {
                _navigationEvents.emit(
                    NavigationEvent.NavigateTo(
                        Screen.Home,
                        popUpToRoute = Screen.Login,
                        inclusive = true
                    )
                )
            }
        } else {
            _loginError.value = "Correo o contraseña incorrectos"
        }
    }

    fun irARegistro() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(Screen.Registro))
        }
    }

    fun irALogin() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(Screen.Login))
        }
    }

    // --- Registro y Navegación ---
    fun validarYEnviar() {
        val state = _uiState.value

        val errCorreo = when {
            state.correo.isBlank() -> "El correo es obligatorio"
            !state.correo.contains("@") -> "Debe ser un correo valido"
            !state.correo.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")) -> "Formato inválido"
            else -> null
        }

        val errClave = when {
            state.clave.length < 8 -> "Mínimo 8 caracteres"
            !state.clave.any { it.isDigit() } -> "Debe incluir al menos un número"
            !state.clave.any { it.isUpperCase() } -> "Debe incluir al menos una mayúscula"
            else -> null
        }

        val errNombre = if (state.nombre.isBlank()) "El nombre es obligatorio" else null
        val errDireccion = if (state.direccion.isBlank()) "La dirección es obligatoria" else null

        val tieneError = errNombre != null || errCorreo != null || errClave != null || errDireccion != null
        _errores.value = UsuarioErrores(errNombre, errCorreo, errClave, errDireccion)

        if (!tieneError) {
            viewModelScope.launch {
                _uiState.update { it.copy(isSaving = true) }
                repository.saveUserData(state.nombre, state.correo, state.clave, state.direccion, state.esEstudiante)
                delay(1000)
                _uiState.update { it.copy(isSaving = false) }
                _navigationEvents.emit(
                    NavigationEvent.NavigateTo(Screen.Home, popUpToRoute = Screen.Registro, inclusive = true)
                )
            }
        }
    }

    fun irAResumen() {
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.NavigateTo(Screen.Resumen))
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            _loginCorreo.value = ""
            _loginClave.value = ""
            _navigationEvents.emit(
                NavigationEvent.NavigateTo(Screen.Login, popUpToRoute = Screen.Home, inclusive = true)
            )
        }
    }
}