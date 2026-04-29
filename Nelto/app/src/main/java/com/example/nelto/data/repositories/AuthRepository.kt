package com.example.nelto.data.repositories

import com.example.nelto.data.api.RetrofitClient
import com.example.nelto.data.models.network.LoginRequest
import com.example.nelto.data.models.network.LoginResponse
import android.util.Log
import com.example.nelto.data.models.network.RegisterRequest
import com.example.nelto.data.models.network.UserData
import com.example.nelto.utils.SessionManager
class AuthRepository(private val sessionManager: SessionManager){
    suspend fun register(
        nombre: String,
        apellidoPaterno: String,
        apellidoMaterno: String,
        email: String,
        password: String,
        alias: String,
        telefono: String,
        avatarBase64: String? = null
    ): LoginResponse? {
        return try {
            Log.d("AUTH_REPO", "📝 Intentando registro para: $email")
            Log.d("AUTH_REPO", "🖼️ Avatar Base64: ${if (avatarBase64 != null) "Si (${avatarBase64?.length} chars)" else "No"}")

            val request = RegisterRequest(
                nombre = nombre,
                apellido_paterno = apellidoPaterno,
                apellido_materno = apellidoMaterno,
                correo = email,
                contrasenia = password,
                alias = alias,
                telefono = telefono,
                avatar = avatarBase64
            )

            val response = RetrofitClient.apiService.register(request)

            Log.d("AUTH_REPO", "📡 Código respuesta registro: ${response.code()}")

            if (response.isSuccessful) {
                Log.d("AUTH_REPO", "✅ Registro exitoso")
                response.body()
            } else {
                Log.e("AUTH_REPO", "❌ Error registro: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Excepción registro: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    suspend fun login(email: String, password: String): LoginResponse? {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getProfile(): UserData? {
        return try {
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.e("AUTH_REPO", "No hay token disponible")
                return null
            }

            Log.d("AUTH_REPO", "🔐 Obteniendo perfil con token")
            val response = RetrofitClient.apiService.getProfile("Bearer $token")

            if (response.isSuccessful && response.body() != null) {
                Log.d("AUTH_REPO", "✅ Perfil obtenido")
                response.body()?.user
            } else {
                Log.e("AUTH_REPO", "❌ Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("AUTH_REPO", "❌ Excepción: ${e.message}")
            null
        }
    }
}