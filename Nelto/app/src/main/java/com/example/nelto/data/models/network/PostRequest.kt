package com.example.nelto.data.models.network

data class PostRequest(
    val Id_usuario: Int,
    val Titulo: String,
    val Descripcion: String,
    val Borrador: Int  // 0 = publicado, 1 = borrador
)