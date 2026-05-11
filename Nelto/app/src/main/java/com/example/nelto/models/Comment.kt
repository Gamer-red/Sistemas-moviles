package com.example.nelto.models

data class Comment(
    val Id_comentario: Int,
    val Id_usuario: Int,
    val Id_publicaciones: Int,
    val Comentario: String,
    val Fecha: String,
    val Hora: String,
    val Alias: String
)