package com.example.nelto.data.models.network

data class LikeRequest(
    val id_publicaciones: Int,
    val tipo: String = "me_gusta"
)