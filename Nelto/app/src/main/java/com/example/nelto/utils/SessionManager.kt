package com.example.nelto.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.example.nelto.data.models.network.UserData
import com.example.nelto.models.Usuario

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NOMBRE = "user_nombre"
        private const val KEY_USER_APELLIDO_PATERNO = "user_apellido_paterno"
        private const val KEY_USER_APELLIDO_MATERNO = "user_apellido_materno"
        private const val KEY_USER_ALIAS = "user_alias"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_TELEFONO = "user_telefono"
        private const val KEY_USER_AVATAR_URI = "user_avatar_uri"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Guardar token JWT
    fun guardarToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)

    // Guardar sesión desde el login (con UserData)
    fun guardarSesionDesdeLogin(userData: UserData) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, userData.Id_usuario)
            putString(KEY_USER_NOMBRE, userData.Nombre)
            putString(KEY_USER_APELLIDO_PATERNO, userData.Apellido_paterno)
            putString(KEY_USER_APELLIDO_MATERNO, userData.Apellido_materno)
            putString(KEY_USER_ALIAS, userData.Alias)
            putString(KEY_USER_EMAIL, userData.Correo)
            putString(KEY_USER_TELEFONO, userData.telefono)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    // Guardar sesión desde registro (con Usuario)
    fun guardarSesion(usuario: Usuario) {
        val editor = prefs.edit()
        editor.putString(KEY_USER_NOMBRE, usuario.nombre)
        editor.putString(KEY_USER_APELLIDO_PATERNO, usuario.apellidoPaterno)
        editor.putString(KEY_USER_APELLIDO_MATERNO, usuario.apellidoMaterno)
        editor.putString(KEY_USER_ALIAS, usuario.alias)
        editor.putString(KEY_USER_EMAIL, usuario.email)
        editor.putString(KEY_USER_TELEFONO, usuario.telefono)
        editor.putString(KEY_USER_AVATAR_URI, usuario.avatarUri?.toString())
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    fun getUsuario(): Usuario {
        return Usuario(

            nombre = prefs.getString(KEY_USER_NOMBRE, "") ?: "",
            apellidoPaterno = prefs.getString(KEY_USER_APELLIDO_PATERNO, "") ?: "",
            apellidoMaterno = prefs.getString(KEY_USER_APELLIDO_MATERNO, "") ?: "",
            alias = prefs.getString(KEY_USER_ALIAS, "") ?: "",
            email = prefs.getString(KEY_USER_EMAIL, "") ?: "",
            telefono = prefs.getString(KEY_USER_TELEFONO, "") ?: "",
            password = "",
            avatarUri = prefs.getString(KEY_USER_AVATAR_URI, null)?.let { Uri.parse(it) }
        )
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, 0)

    fun getUserAlias(): String = prefs.getString(KEY_USER_ALIAS, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun cerrarSesion() {
        prefs.edit().clear().apply()
    }
}