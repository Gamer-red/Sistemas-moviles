package com.example.nelto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import okhttp3.OkHttpClient  // ← IMPORTANTE: Agregar este import
import okhttp3.Request        // ← IMPORTANTE: Agregar este import

class LoginActivity : AppCompatActivity() {
    private lateinit var authRepository: AuthRepository
    private lateinit var sessionManager: SessionManager

    private fun probarConexionBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://10.0.2.2:3000/")  // ← Agregar /api/test
                    .get()
                    .build()
                val response = client.newCall(request).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        Toast.makeText(this@LoginActivity, "✅ Conectado: $body", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "❌ Error: ${response.code}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "❌ ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)  // ← PRIMERO setContentView

        authRepository = AuthRepository()
        sessionManager = SessionManager(this)

        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnRegister = findViewById<MaterialButton>(R.id.btnRegister)

        // ← AHORA buscar el botón después de setContentView
        val btnTest = findViewById<MaterialButton>(R.id.btnTestConexion)
        if (btnTest != null) {
            btnTest.setOnClickListener {
                probarConexionBackend()
            }
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            btnLogin.isEnabled = false

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = authRepository.login(email, password)

                    withContext(Dispatchers.Main) {
                        btnLogin.isEnabled = true
                        if (result != null) {
                            sessionManager.guardarToken(result.token)
                            sessionManager.guardarSesionDesdeLogin(result.user)

                            Toast.makeText(
                                this@LoginActivity,
                                "Bienvenido ${result.user.Nombre}",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Credenciales inválidas",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        btnLogin.isEnabled = true
                        Log.e("LOGIN_ERROR", "Error: ${e.message}")
                        Toast.makeText(
                            this@LoginActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}