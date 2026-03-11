package com.example.nelto


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val btnGuardar = findViewById<MaterialButton>(R.id.btnEditGuardar)
        val btnCancelar = findViewById<MaterialButton>(R.id.btnEditCancelar)
        val btnSeleccionarAvatar = findViewById<MaterialButton>(R.id.btnEditSeleccionarAvatar)
        btnGuardar.setOnClickListener {
            Toast.makeText(this, "Funcionalidad pendiente: Guardar cambios", Toast.LENGTH_SHORT).show()
        }

        btnCancelar.setOnClickListener {
            Toast.makeText(this, "Cancelar edición", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSeleccionarAvatar.setOnClickListener {
            Toast.makeText(this, "Funcionalidad pendiente: Seleccionar imagen", Toast.LENGTH_SHORT).show()
        }
    }
}