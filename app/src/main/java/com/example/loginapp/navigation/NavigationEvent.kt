package com.example.loginapp.navigation

sealed class NavigationEvent {
    data class NavigateTo(
        val route: Screen,
        val popUpToRoute: Screen? = null,
        val inclusive: Boolean = false
    ) : NavigationEvent()
    object PopBackStack : NavigationEvent()
}