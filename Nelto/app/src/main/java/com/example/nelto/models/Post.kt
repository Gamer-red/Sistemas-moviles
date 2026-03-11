package com.example.nelto.models

import android.net.Uri
import java.util.Date

/**
 * Modelo que representa una publicación en la red social
 */
data class Post(
    // Campos que vienen de la BD
    val id_publicaciones: Int = 0,
    val Id_usuario: Int = 0,
    val Titulo: String = "",
    val Descripcion: String = "",
    val Fecha_creacion: String = "",
    val Fecha_modificacion: String = "",
    val Borrador: Int = 0,

    // Campos adicionales para la UI (no vienen de la BD)
    val usuarioAlias: String = "",
    var usuarioDioLike: Boolean = false,
    var likes: Int = 0,
    var comentarios: Int = 0
)