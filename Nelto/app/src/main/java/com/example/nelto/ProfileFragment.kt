package com.example.nelto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import android.util.Log
import com.example.nelto.utils.SessionManager
import com.example.nelto.data.repositories.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.graphics.BitmapFactory
import android.util.Base64

class ProfileFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvNombre: TextView
    private lateinit var tvAlias: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var btnEditarPerfil: MaterialButton
    private lateinit var btnMisPublicaciones: MaterialButton
    private lateinit var btnPublicacionesGuardadas: MaterialButton

    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(view)
        setupListeners()
        cargarDatosUsuario()

        return view
    }

    private fun initViews(view: View) {
        ivAvatar = view.findViewById(R.id.ivProfileAvatar)
        tvNombre = view.findViewById(R.id.tvProfileNombre)
        tvAlias = view.findViewById(R.id.tvProfileAlias)
        tvEmail = view.findViewById(R.id.tvProfileEmail)
        tvTelefono = view.findViewById(R.id.tvProfileTelefono)
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)
        btnMisPublicaciones = view.findViewById(R.id.btnMisPublicaciones)
        btnPublicacionesGuardadas = view.findViewById(R.id.btnPublicacionesGuardadas)

        sessionManager = SessionManager(requireContext())
        authRepository = AuthRepository(sessionManager)
    }

    private fun cargarDatosUsuario() {
        if (!sessionManager.isLoggedIn()) {
            Log.e("ProfileFragment", "No hay usuario logueado")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val user = authRepository.getProfile()

            withContext(Dispatchers.Main) {
                if (user != null) {
                    mostrarDatosUsuario(user)
                } else {
                    Log.e("ProfileFragment", "Error al cargar perfil")
                    mostrarDatosPorDefecto()
                }
            }
        }
    }

    private fun mostrarDatosUsuario(user: com.example.nelto.data.models.network.UserData) {
        tvNombre.text = "${user.Nombre} ${user.Apellido_paterno} ${user.Apellido_materno}"
        tvAlias.text = "@${user.Alias}"
        tvEmail.text = user.Correo
        tvTelefono.text = user.telefono

        // Cargar avatar si existe
        user.Avatar?.let { avatarBase64 ->
            try {
                val cleanBase64 = if (avatarBase64.contains("base64,")) {
                    avatarBase64.substringAfter("base64,")
                } else {
                    avatarBase64
                }
                val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ivAvatar.setImageBitmap(bitmap)
                Log.d("ProfileFragment", "Avatar cargado correctamente")
            } catch (e: Exception) {
                Log.e("ProfileFragment", "Error al cargar avatar: ${e.message}")
            }
        }
    }

    private fun mostrarDatosPorDefecto() {
        tvNombre.text = "Usuario"
        tvAlias.text = "@usuario"
        tvEmail.text = "No disponible"
        tvTelefono.text = "No disponible"
    }

    private fun setupListeners() {
        btnEditarPerfil.setOnClickListener {
            startActivity(
                android.content.Intent(
                    requireContext(),
                    EditProfileActivity::class.java
                )
            )
        }

        btnMisPublicaciones.setOnClickListener {
            com.google.android.material.snackbar.Snackbar.make(
                requireView(),
                "Funcionalidad pendiente",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }

        btnPublicacionesGuardadas.setOnClickListener {
            com.google.android.material.snackbar.Snackbar.make(
                requireView(),
                "Funcionalidad pendiente",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}