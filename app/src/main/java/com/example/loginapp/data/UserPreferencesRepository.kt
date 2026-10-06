package com.example.loginapp.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_settings")

class UserPreferencesRepository(private val context: Context) {
    companion object {
        val KEY_NOMBRE = stringPreferencesKey("user_nombre")
        val KEY_CORREO = stringPreferencesKey("user_correo")
        val KEY_CLAVE = stringPreferencesKey("user_clave")
        val KEY_DIRECCION = stringPreferencesKey("user_direccion")
        val KEY_ESTUDIANTE = booleanPreferencesKey("user_estudiante")
    }

    val savedNombre: Flow<String> = context.dataStore.data.map { it[KEY_NOMBRE] ?: "" }
    val savedCorreo: Flow<String> = context.dataStore.data.map { it[KEY_CORREO] ?: "" }
    val savedClave: Flow<String> = context.dataStore.data.map { it[KEY_CLAVE] ?: "" }
    val savedDireccion: Flow<String> = context.dataStore.data.map { it[KEY_DIRECCION] ?: "" }
    val savedEstudiante: Flow<Boolean> = context.dataStore.data.map { it[KEY_ESTUDIANTE] ?: true }

    suspend fun saveUserData(nombre: String, correo: String, clave: String, direccion: String, esEstudiante: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NOMBRE] = nombre
            prefs[KEY_CORREO] = correo
            prefs[KEY_CLAVE] = clave
            prefs[KEY_DIRECCION] = direccion
            prefs[KEY_ESTUDIANTE] = esEstudiante
        }
    }
}