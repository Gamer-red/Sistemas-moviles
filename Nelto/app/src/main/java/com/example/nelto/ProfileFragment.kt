package com.example.nelto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.example.nelto.adapters.PostAdapter
import com.example.nelto.models.Post

class ProfileFragment : Fragment() {

    class ProfileFragment : Fragment() {

        private lateinit var ivAvatar: ImageView
        private lateinit var tvNombre: TextView
        private lateinit var tvAlias: TextView
        private lateinit var tvEmail: TextView
        private lateinit var tvTelefono: TextView
        private lateinit var tvStatsPosts: TextView
        private lateinit var tvStatsLikes: TextView
        private lateinit var btnEditarPerfil: MaterialButton
        private lateinit var tabLayout: TabLayout
        private lateinit var recyclerView: RecyclerView


        private lateinit var postAdapter: PostAdapter
        private val publicacionesEjemplo = mutableListOf<Post>()
        private val borradoresEjemplo = mutableListOf<Post>()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_profile, container, false)

            initViews(view)
            cargarDatosEjemplo()
            setupTabs()
            setupRecyclerView()
            setupListeners()

            return view
        }

        private fun initViews(view: View) {
            ivAvatar = view.findViewById(R.id.ivProfileAvatar)
            tvNombre = view.findViewById(R.id.tvProfileNombre)
            tvAlias = view.findViewById(R.id.tvProfileAlias)
            tvEmail = view.findViewById(R.id.tvProfileEmail)
            tvTelefono = view.findViewById(R.id.tvProfileTelefono)
            btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)
        }

        private fun cargarDatosEjemplo() {
            // Datos de ejemplo estáticos para mostrar en UI
            tvNombre.text = "Juan Pérez García"
            tvAlias.text = "@juanito"
            tvEmail.text = "juan.perez@ejemplo.com"
            tvTelefono.text = "555-123-4567"

            tvStatsPosts.text = "5"
            tvStatsLikes.text = "23"

            publicacionesEjemplo.addAll(
                listOf(
                    Post(
                        id_publicaciones = 1,
                        Id_usuario = 101,
                        Titulo = "Mi primer día en la escuela",
                        Descripcion = "Hoy conocí a muchos compañeros nuevos en clase. ¡Excelente experiencia!",
                        Fecha_creacion = "2024-03-09",
                        Fecha_modificacion = "2024-03-09",
                        Borrador = 0,
                        usuarioAlias = "juanito",
                        likes = 15,
                        comentarios = 3,
                        usuarioDioLike = false
                    ),
                    Post(
                        id_publicaciones = 2,
                        Id_usuario = 101,
                        Titulo = "Proyecto de programación",
                        Descripcion = "Buscando equipo para el proyecto de Android Studio",
                        Fecha_creacion = "2024-03-08",
                        Fecha_modificacion = "2024-03-08",
                        Borrador = 0,
                        usuarioAlias = "juanito",
                        likes = 8,
                        comentarios = 5,
                        usuarioDioLike = false
                    ),
                    Post(
                        id_publicaciones = 3,
                        Id_usuario = 101,
                        Titulo = "Fin de semana",
                        Descripcion = "Compartiendo fotos del fin de semana",
                        Fecha_creacion = "2024-03-07",
                        Fecha_modificacion = "2024-03-07",
                        Borrador = 0,
                        usuarioAlias = "juanito",
                        likes = 12,
                        comentarios = 2,
                        usuarioDioLike = false,
                    )
                )
            )

            // Crear borradores de ejemplo
            borradoresEjemplo.addAll(
                listOf(
                    Post(
                        id_publicaciones = 4,
                        Id_usuario = 101,
                        Titulo = "Borrador: Ideas para nuevo post",
                        Descripcion = "Este es un post que aún no he publicado. Contiene ideas para un proyecto futuro.",
                        Fecha_creacion = "2024-03-06",
                        Fecha_modificacion = "2024-03-06",
                        Borrador = 1,
                        usuarioAlias = "juanito",
                        likes = 0,
                        comentarios = 0,
                        usuarioDioLike = false
                    ),
                    Post(
                        id_publicaciones = 5,
                        Id_usuario = 101,
                        Titulo = "Borrador: Reflexiones",
                        Descripcion = "Algunas reflexiones personales que quiero pulir antes de publicar",
                        Fecha_creacion = "2024-03-05",
                        Fecha_modificacion = "2024-03-05",
                        Borrador = 1,
                        usuarioAlias = "juanito",
                        likes = 0,
                        comentarios = 0,
                        usuarioDioLike = false
                    )
                )
            )
        }

        private fun setupTabs() {
            tabLayout.addTab(tabLayout.newTab().setText("Publicaciones"))
            tabLayout.addTab(tabLayout.newTab().setText("Borradores"))

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> mostrarPublicaciones()
                        1 -> mostrarBorradores()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }

        private fun setupRecyclerView() {
            postAdapter = PostAdapter(
                posts = publicacionesEjemplo, // Mostrar publicaciones por defecto
                onLikeClick = { post ->
                    // Solo UI - mensaje informativo
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Funcionalidad pendiente: Dar like",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onCommentClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Funcionalidad pendiente: Comentarios",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onPostClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Funcionalidad pendiente: Ver detalle",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = postAdapter
        }

        private fun mostrarPublicaciones() {
            postAdapter.updatePosts(publicacionesEjemplo)
        }

        private fun mostrarBorradores() {
            postAdapter.updatePosts(borradoresEjemplo)
        }

        private fun setupListeners() {
            btnEditarPerfil.setOnClickListener {
                // Abrir pantalla de edición (solo UI)
                startActivity(
                    android.content.Intent(
                        requireContext(),
                        EditProfileActivity::class.java
                    )
                )
            }
        }
    }
}