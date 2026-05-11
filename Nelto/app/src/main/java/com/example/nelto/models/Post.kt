package com.example.nelto.models

import android.net.Uri

data class Post(
    val id_publicaciones: Int,
    val Id_usuario: Int,
    val Titulo: String,
    val Descripcion: String,
    val Fecha_creacion: String,
    val Fecha_modificacion: String?,
    val Borrador: Int,
    val Alias: String,              // ← El backend devuelve "Alias"
    val Nombre: String,             // ← El backend devuelve "Nombre"
    val likes: Int = 0,
    val commentsCount: Int = 0,
    var usuarioDioLike: Boolean = false,
    var guardado: Boolean = false
) {
    // Propiedad calculada para compatibilidad con el adaptador existente
    val usuarioAlias: String get() = Alias
    val usuarioNombre: String get() = Nombre
}