package com.example.nelto.data.models.network

data class CreateCommentRequest(
    val comentario: String,
    val id_publicaciones: Int
)