package com.example.loginapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginapp.viewmodels.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Resumen de Registro",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                HorizontalDivider()

                Text(text = "Nombre: ${uiState.nombre}", fontSize = 16.sp)
                Text(text = "Correo: ${uiState.correo}", fontSize = 16.sp)
                Text(text = "Dirección: ${uiState.direccion}", fontSize = 16.sp)
                Text(
                    text = "Estado Alumno: ${if (uiState.esEstudiante) "Activo" else "Inactivo"}",
                    fontSize = 16.sp
                )
            }
        }
    }
}