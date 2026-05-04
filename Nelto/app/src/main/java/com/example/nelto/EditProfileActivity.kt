package com.example.nelto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nelto.data.repositories.AuthRepository
import com.example.nelto.utils.SessionManager
import com.example.nelto.data.models.network.UserData
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import android.widget.ImageView
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap

class EditProfileActivity : AppCompatActivity() {

    private lateinit var ivAvatar: ImageView
    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellidoPaterno: TextInputEditText
    private lateinit var etApellidoMaterno: TextInputEditText
    private lateinit var etAlias: TextInputEditText
    private lateinit var etTelefono: TextInputEditText
    private lateinit var etEmail: TextInputEditText          // ← Agregar
    private lateinit var btnSeleccionarAvatar: MaterialButton
    private lateinit var btnGuardar: MaterialButton
    private lateinit var btnCancelar: MaterialButton

    private lateinit var sessionManager: SessionManager
    private lateinit var authRepository: AuthRepository
    private var avatarBase64: String? = null
    private var usuarioActual: UserData? = null

    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            if (uri != null) {
                avatarBase64 = uriToBase64(uri)
                ivAvatar.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sessionManager = SessionManager(this)
        authRepository = AuthRepository(sessionManager)

        initViews()
        cargarDatosActuales()  // ← Cargar datos del usuario
        setupListeners()
    }

    private fun initViews() {
        ivAvatar = findViewById(R.id.ivEditAvatar)
        etNombre = findViewById(R.id.etEditNombre)
        etApellidoPaterno = findViewById(R.id.etEditApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etEditApellidoMaterno)
        etAlias = findViewById(R.id.etEditAlias)
        etTelefono = findViewById(R.id.etEditTelefono)
        etEmail = findViewById(R.id.etEditEmail)           // ← Inicializar
        btnSeleccionarAvatar = findViewById(R.id.btnEditSeleccionarAvatar)
        btnGuardar = findViewById(R.id.btnEditGuardar)
        btnCancelar = findViewById(R.id.btnEditCancelar)
    }

    private fun cargarDatosActuales() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = authRepository.getProfile()
            withContext(Dispatchers.Main) {
                if (user != null) {
                    usuarioActual = user

                    // Cargar datos en los campos
                    etNombre.setText(user.Nombre)
                    etApellidoPaterno.setText(user.Apellido_paterno)
                    etApellidoMaterno.setText(user.Apellido_materno)
                    etAlias.setText(user.Alias)
                    etTelefono.setText(user.telefono)
                    etEmail.setText(user.Correo)           // ← Cargar correo

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
                        } catch (e: Exception) {
                            Log.e("EditProfile", "Error al cargar avatar: ${e.message}")
                        }
                    }
                } else {
                    Log.e("EditProfile", "No se pudo cargar el perfil")
                    Toast.makeText(this@EditProfileActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)
            val baos = ByteArrayOutputStream()
            scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, baos)
            val imageBytes = baos.toByteArray()
            val base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
            "data:image/jpeg;base64,$base64"
        } catch (e: Exception) {
            Log.e("EditProfile", "Error al convertir imagen: ${e.message}")
            null
        }
    }

    private fun setupListeners() {
        btnSeleccionarAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            seleccionarImagen.launch(intent)
        }

        btnGuardar.setOnClickListener {
            guardarCambios()
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun guardarCambios() {
        val nombre = etNombre.text.toString().trim()
        val apellidoPaterno = etApellidoPaterno.text.toString().trim()
        val apellidoMaterno = etApellidoMaterno.text.toString().trim()
        val alias = etAlias.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val correo = etEmail.text.toString().trim()

        if (nombre.isEmpty() || apellidoPaterno.isEmpty() || alias.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            Toast.makeText(this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        btnGuardar.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            val result = authRepository.updateProfile(
                nombre = nombre,
                apellidoPaterno = apellidoPaterno,
                apellidoMaterno = apellidoMaterno,
                alias = alias,
                telefono = telefono,
                correo = correo,
                avatarBase64 = avatarBase64
            )

            withContext(Dispatchers.Main) {
                btnGuardar.isEnabled = true

                if (result != null) {
                    Toast.makeText(this@EditProfileActivity, "Perfil actualizado", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@EditProfileActivity, "Error al actualizar", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}