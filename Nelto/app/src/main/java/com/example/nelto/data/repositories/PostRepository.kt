package com.example.nelto.data.repositories

import android.util.Log
import com.example.nelto.data.api.RetrofitClient
import com.example.nelto.data.models.network.PostsResponse
import com.example.nelto.data.models.network.PostRequest
import com.example.nelto.models.Post
import com.example.nelto.utils.SessionManager
import com.example.nelto.data.models.network.LikeRequest
import com.example.nelto.data.models.network.LikeResponse
import com.example.nelto.models.Comment
import com.example.nelto.data.models.network.CreateCommentRequest

class PostRepository(private val sessionManager: SessionManager) {

    suspend fun getPublicaciones(page: Int = 1, limit: Int = 10): PostsResponse? {
        return try {
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.e("PostRepository", "❌ No hay token disponible")
                return null
            }

            val response = RetrofitClient.apiService.getPublicaciones("Bearer $token", page, limit)
            if (response.isSuccessful && response.body() != null) {
                Log.d(
                    "PostRepository",
                    "✅ Publicaciones obtenidas: ${response.body()?.posts?.size}"
                )
                response.body()
            } else {
                Log.e("PostRepository", "❌ Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "❌ Excepción: ${e.message}")
            null
        }
    }

    suspend fun toggleLike(postId: Int, userId: Int): LikeResponse? {
        return try {
            val token = sessionManager.getToken()
            Log.d("PostRepository", "🔑 Token: ${if (!token.isNullOrEmpty()) "Presente" else "Nulo"}")

            if (token.isNullOrEmpty()) {
                Log.e("PostRepository", "❌ No hay token disponible")
                return null
            }

            val request = LikeRequest(id_publicaciones = postId)
            Log.d("PostRepository", "📤 Enviando request: $request")

            val response = RetrofitClient.apiService.toggleLike("Bearer $token", request)

            Log.d("PostRepository", "📡 Código respuesta: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                Log.d("PostRepository", "✅ Like toggled: ${response.body()?.action}")
                response.body()
            } else {
                Log.e("PostRepository", "❌ Error: ${response.code()} - ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "❌ Excepción: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun getCommentsByPost(postId: Int): List<Comment> {
        return try {
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.e("PostRepository", "No hay token disponible")
                return emptyList()
            }

            val response = RetrofitClient.apiService.getCommentsByPost("Bearer $token", postId)
            if (response.isSuccessful && response.body() != null) {
                Log.d("PostRepository", "✅ Comentarios obtenidos: ${response.body()?.comments?.size}")
                response.body()?.comments ?: emptyList()
            } else {
                Log.e("PostRepository", "❌ Error: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "❌ Excepción: ${e.message}")
            emptyList()
        }
    }

    suspend fun createComment(postId: Int, comentario: String): Comment? {
        return try {
            val token = sessionManager.getToken()
            if (token.isNullOrEmpty()) {
                Log.e("PostRepository", "No hay token disponible")
                return null
            }

            val request = CreateCommentRequest(comentario = comentario, id_publicaciones = postId)
            val response = RetrofitClient.apiService.createComment("Bearer $token", request)

            if (response.isSuccessful && response.body() != null) {
                Log.d("PostRepository", "✅ Comentario creado")
                response.body()?.comment
            } else {
                Log.e("PostRepository", "❌ Error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "❌ Excepción: ${e.message}")
            null
        }
    }

}