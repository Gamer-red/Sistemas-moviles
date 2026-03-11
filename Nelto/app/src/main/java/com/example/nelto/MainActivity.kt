package com.example.nelto

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var topAppBar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupNavigation()
        setupTopAppBar()
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        topAppBar = findViewById(R.id.topAppBar)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController


        bottomNavigationView.setupWithNavController(navController)


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_posts -> {
                    navController.navigate(R.id.postsFragment)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.navigation_logout -> {
                    mostrarDialogoConfirmacionSalir()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupTopAppBar() {
        setSupportActionBar(topAppBar)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_search -> {
                    // TODO: Implementar búsqueda
                    true
                }
                R.id.action_notifications -> {
                    // TODO: Mostrar notificaciones
                    true
                }
                else -> false
            }
        }
    }

    private fun mostrarDialogoConfirmacionSalir() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
            .setPositiveButton("Sí, salir") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        // TODO: Limpiar datos de sesión (SharedPreferences, etc.)

        // Redirigir al Login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}