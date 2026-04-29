package com.example.nelto.data.models.network

data class RegisterRequest(
    val nombre: String,
    val apellido_paterno: String,
    val apellido_materno: String,
    val correo: String,
    val contrasenia: String,
    val alias: String,
    val telefono: String,
    val avatar: String? = null  // Base64 de la imagen (opcional)
)