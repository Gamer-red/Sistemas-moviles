package com.example.nelto.data.repositories

import com.example.nelto.data.api.RetrofitClient
import com.example.nelto.data.models.network.LoginRequest
import com.example.nelto.data.models.network.LoginResponse

class AuthRepository {

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
}