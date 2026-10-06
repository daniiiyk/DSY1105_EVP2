package com.example.loginapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.loginapp.navigation.NavigationEvent
import com.example.loginapp.navigation.Screen
import com.example.loginapp.ui.screens.HomeScreen
import com.example.loginapp.ui.screens.LoginScreen
import com.example.loginapp.ui.screens.RegistroScreen
import com.example.loginapp.ui.screens.ResumenScreen
import com.example.loginapp.ui.theme.LoginAppTheme
import com.example.loginapp.viewmodels.UsuarioViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginAppTheme {
                val navController = rememberNavController()
                val userViewModel: UsuarioViewModel = viewModel()

                LaunchedEffect(Unit) {
                    userViewModel.navigationEvents.collectLatest { event ->
                        when (event) {
                            is NavigationEvent.NavigateTo -> {
                                navController.navigate(event.route.route) {
                                    event.popUpToRoute?.let {
                                        popUpTo(it.route) { inclusive = event.inclusive }
                                    }
                                }
                            }
                            is NavigationEvent.PopBackStack -> {
                                navController.popBackStack()
                            }
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Login.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(viewModel = userViewModel)
                        }
                        composable(Screen.Registro.route) {
                            RegistroScreen(viewModel = userViewModel)
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(viewModel = userViewModel)
                        }
                        composable(Screen.Resumen.route) {
                            ResumenScreen(viewModel = userViewModel)
                        }
                    }
                }
            }
        }
    }
}