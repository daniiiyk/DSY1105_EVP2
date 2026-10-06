package com.example.loginapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Registro : Screen("registro")
    object Home : Screen("home")
    object Resumen : Screen("resumen")
}