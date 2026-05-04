package com.example.nelto.data.models.network

data class UpdateProfileRequest(
    val nombre: String? = null,
    val apellido_paterno: String? = null,
    val apellido_materno: String? = null,
    val alias: String? = null,
    val telefono: String? = null,
    val correo: String? = null,
    val avatar: String? = null
)