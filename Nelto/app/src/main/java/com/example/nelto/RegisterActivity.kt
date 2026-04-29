package com.example.nelto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.nelto.data.repositories.AuthRepository
import com.example.nelto.utils.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Bitmap

class RegisterActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository
    private lateinit var sessionManager: SessionManager
    private var avatarBase64: String? = null

    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            if (uri != null) {
                try {
                    // Convertir imagen a Base64
                    val inputStream = contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    // Redimensionar imagen para evitar que sea demasiado grande
                    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true)

                    val baos = ByteArrayOutputStream()
                    scaledBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 70, baos)
                    val imageBytes = baos.toByteArray()
                    avatarBase64 = android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)

                    Log.d("REGISTER_IMG", "✅ Imagen convertida a Base64")
                    Log.d("REGISTER_IMG", "📏 Tamaño Base64: ${avatarBase64?.length} caracteres")

                    // Mostrar preview
                    findViewById<android.widget.ImageView>(R.id.ivAvatar).setImageURI(uri)
                } catch (e: Exception) {
                    Log.e("REGISTER_IMG", "❌ Error al procesar imagen: ${e.message}")
                    Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        sessionManager = SessionManager(this)
        authRepository = AuthRepository(sessionManager)

        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val etApellidoPaterno = findViewById<TextInputEditText>(R.id.etApellidoPaterno)
        val etApellidoMaterno = findViewById<TextInputEditText>(R.id.etApellidoMaterno)
        val etAlias = findViewById<TextInputEditText>(R.id.etAlias)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etTelefono = findViewById<TextInputEditText>(R.id.etTelefono)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnRegistrar = findViewById<MaterialButton>(R.id.btnRegistrar)
        val btnSeleccionarAvatar = findViewById<MaterialButton>(R.id.btnSeleccionarAvatar)

        btnSeleccionarAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            seleccionarImagen.launch(intent)
        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellidoPaterno = etApellidoPaterno.text.toString().trim()
            val apellidoMaterno = etApellidoMaterno.text.toString().trim()
            val alias = etAlias.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty() || apellidoPaterno.isEmpty() || alias.isEmpty() ||
                email.isEmpty() || telefono.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnRegistrar.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = authRepository.register(
                        nombre = nombre,
                        apellidoPaterno = apellidoPaterno,
                        apellidoMaterno = apellidoMaterno,
                        email = email,
                        password = password,
                        alias = alias,
                        telefono = telefono,
                        avatarBase64 = avatarBase64
                    )

                    withContext(Dispatchers.Main) {
                        btnRegistrar.isEnabled = true

                        if (result != null) {
                            // Guardar sesión
                            sessionManager.guardarToken(result.token)
                            sessionManager.guardarSesionDesdeLogin(result.user)

                            Toast.makeText(
                                this@RegisterActivity,
                                "¡Registro exitoso! Bienvenido ${result.user.Nombre}",
                                Toast.LENGTH_LONG
                            ).show()

                            // Ir a MainActivity
                            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Error en el registro. El correo podría estar duplicado.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        btnRegistrar.isEnabled = true
                        Log.e("REGISTER_ERROR", "Error: ${e.message}")
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}