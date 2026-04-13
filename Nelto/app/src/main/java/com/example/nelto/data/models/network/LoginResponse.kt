package com.example.nelto.data.models.network

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserData
)

data class UserData(
    val Id_usuario: Int,
    val Nombre: String,
    val Apellido_paterno: String,
    val Apellido_materno: String,
    val Correo: String,
    val Alias: String,
    val telefono: String,
    val Avatar: String? = null
)