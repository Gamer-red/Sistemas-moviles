package com.example.nelto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.example.nelto.models.Usuario


class RegisterActivity : AppCompatActivity() {

    private var avatarUri: Uri? = null
    private lateinit var ivAvatar: ImageView

    // Registro para seleccionar imagen de la galería
    private val seleccionarImagen = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            avatarUri = data?.data
            // Mostrar la imagen seleccionada
            ivAvatar.setImageURI(avatarUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initViews()
        setupListeners()
    }

    private fun initViews() {
        ivAvatar = findViewById(R.id.ivAvatar)
        // Resto de vistas
    }

    private fun setupListeners() {
        findViewById<MaterialButton>(R.id.btnSeleccionarAvatar).setOnClickListener {
            abrirGaleria()
        }

        findViewById<MaterialButton>(R.id.btnRegistrar).setOnClickListener {
            validarYRegistrar()
        }

        findViewById<MaterialButton>(R.id.btnIrALogin).setOnClickListener {
            finish() // Vuelve a la pantalla anterior (Login)
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        seleccionarImagen.launch(intent)
    }

    private fun validarYRegistrar() {
        // Obtener valores de los campos
        val nombre = findViewById<TextInputEditText>(R.id.etNombre).text.toString().trim()
        val apellidoPaterno = findViewById<TextInputEditText>(R.id.etApellidoPaterno).text.toString().trim()
        val apellidoMaterno = findViewById<TextInputEditText>(R.id.etApellidoMaterno).text.toString().trim()
        val alias = findViewById<TextInputEditText>(R.id.etAlias).text.toString().trim()
        val email = findViewById<TextInputEditText>(R.id.etEmail).text.toString().trim()
        val telefono = findViewById<TextInputEditText>(R.id.etTelefono).text.toString().trim()
        val password = findViewById<TextInputEditText>(R.id.etPassword).text.toString().trim()

        // ✅ Declaramos la variable de control
        var isValid = true

        // Validaciones
        if (nombre.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etNombre).error = "Nombre requerido"
            isValid = false
        }

        if (apellidoPaterno.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etApellidoPaterno).error = "Apellido paterno requerido"
            isValid = false
        }



        if (alias.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etAlias).error = "Alias requerido"
            isValid = false
        }

        if (email.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etEmail).error = "Correo requerido"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            findViewById<TextInputEditText>(R.id.etEmail).error = "Correo inválido"
            isValid = false
        }

        if (telefono.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etTelefono).error = "Teléfono requerido"
            isValid = false
        } else if (telefono.length < 10) {
            findViewById<TextInputEditText>(R.id.etTelefono).error = "Teléfono inválido (mínimo 10 dígitos)"
            isValid = false
        }

        if (password.isEmpty()) {
            findViewById<TextInputEditText>(R.id.etPassword).error = "Contraseña requerida"
            isValid = false
        } else if (password.length < 6) {
            findViewById<TextInputEditText>(R.id.etPassword).error = "Mínimo 6 caracteres"
            isValid = false
        }


        if (isValid) {
            val nuevoUsuario = Usuario(
                nombre = nombre,
                apellidoPaterno = apellidoPaterno,
                apellidoMaterno = apellidoMaterno,
                alias = alias,
                email = email,
                telefono = telefono,
                password = password,
                avatarUri = avatarUri
            )

            Toast.makeText(this, "¡Registro exitoso! Bienvenido $alias", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}