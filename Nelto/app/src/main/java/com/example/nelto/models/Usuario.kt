package com.example.nelto.models
import android.net.Uri

data class Usuario(
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val alias: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    var avatarUri: Uri? = null
)
