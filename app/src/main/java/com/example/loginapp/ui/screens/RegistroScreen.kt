package com.example.loginapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loginapp.viewmodels.UsuarioViewModel

@Composable
fun RegistroScreen(viewModel: UsuarioViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val errores by viewModel.errores.collectAsState()
    var passVisible by remember { mutableStateOf(false) }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF1E293B))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Formulario de Registro",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.nombre,
                        onValueChange = { viewModel.onNombreChange(it) },
                        label = { Text("Nombre Completo") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        isError = errores.nombreError != null,
                        supportingText = { errores.nombreError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = uiState.correo,
                        onValueChange = { viewModel.onCorreoChange(it) },
                        label = { Text("Correo Electrónico") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = errores.correoError != null,
                        supportingText = { errores.correoError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = uiState.clave,
                        onValueChange = { viewModel.onClaveChange(it) },
                        label = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passVisible = !passVisible }) {
                                Icon(
                                    imageVector = if (passVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = errores.claveError != null,
                        supportingText = { errores.claveError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = uiState.direccion,
                        onValueChange = { viewModel.onDireccionChange(it) },
                        label = { Text("Dirección") },
                        leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                        isError = errores.direccionError != null,
                        supportingText = { errores.direccionError?.let { Text(it) } },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = uiState.esEstudiante,
                            onCheckedChange = { viewModel.onEstudianteChange(it) }
                        )
                        Text(text = "Alumno Activo", fontSize = 14.sp)
                    }

                    Button(
                        onClick = { viewModel.validarYEnviar() },
                        enabled = !uiState.isSaving,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(text = "Guardar y Continuar", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}