package com.example.nelto.data.api

import com.example.nelto.data.models.network.LoginRequest
import com.example.nelto.data.models.network.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>
}