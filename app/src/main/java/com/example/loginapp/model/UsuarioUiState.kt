package com.example.loginapp.model

data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val direccion: String = "",
    val esEstudiante: Boolean = true,
    val isSaving: Boolean = false
)

data class UsuarioErrores(
    val nombreError: String? = null,
    val correoError: String? = null,
    val claveError: String? = null,
    val direccionError: String? = null
)