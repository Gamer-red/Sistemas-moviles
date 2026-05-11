package com.example.nelto.data.api

import com.example.nelto.data.models.network.LoginRequest
import com.example.nelto.data.models.network.LoginResponse
import com.example.nelto.data.models.network.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Header
import com.example.nelto.data.models.network.UpdateProfileRequest
import com.example.nelto.data.models.network.ProfileResponse
import retrofit2.http.Query
import com.example.nelto.data.models.network.PostsResponse
import com.example.nelto.data.models.network.LikeResponse
import com.example.nelto.data.models.network.LikeRequest
import com.example.nelto.data.models.network.CommentsResponse
import com.example.nelto.data.models.network.CreateCommentRequest
import com.example.nelto.data.models.network.CreateCommentResponse

interface ApiService {

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<LoginResponse>

    @GET("api/users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<ProfileResponse>

    @PUT("api/users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<ProfileResponse>

    @GET("api/posts")
    suspend fun getPublicaciones(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<PostsResponse>

    @POST("api/reactions/insertarReaccion")
    suspend fun toggleLike(
        @Header("Authorization") token: String,
        @Body request: LikeRequest
    ): Response<LikeResponse>

    @GET("api/comments/post/{postId}")
    suspend fun getCommentsByPost(
        @Header("Authorization") token: String,
        @Path("postId") postId: Int
    ): Response<CommentsResponse>

    @POST("api/comments/")
    suspend fun createComment(
        @Header("Authorization") token: String,
        @Body request: CreateCommentRequest
    ): Response<CreateCommentResponse>

}