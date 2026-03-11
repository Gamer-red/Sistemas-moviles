package com.example.nelto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import  com.example.nelto.adapters.PostAdapter
import  com.example.nelto.models.Post

class PostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabNuevoPost: FloatingActionButton
    private lateinit var postAdapter: PostAdapter  // lateinit está bien

    // Lista de posts
    private val postsList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)
        fabNuevoPost = view.findViewById(R.id.fabNuevoPost)

        setupRecyclerView()


        setupListeners()

        cargarPostsEjemplo()

        return view
    }

    private fun setupRecyclerView() {

        postAdapter = PostAdapter(
            posts = postsList,
            onLikeClick = { post -> manejarLike(post) },
            onCommentClick = { post -> abrirComentarios(post) },
            onPostClick = { post -> verPostDetalle(post) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter
    }

    private fun setupListeners() {
        fabNuevoPost.setOnClickListener {

        }
    }

    private fun cargarPostsEjemplo() {



        postsList.clear()


        postsList.addAll(
            listOf(
                Post(
                    id_publicaciones = 1,
                    Id_usuario = 101,
                    Titulo = "¡Hola a todos!",
                    Descripcion = "Esta es mi primera publicación en la red social escolar.",
                    Fecha_creacion = "2024-03-09",
                    Fecha_modificacion = "2024-03-09",
                    Borrador = 0,
                    usuarioAlias = "juanito",
                    likes = 5,
                    comentarios = 2,
                    usuarioDioLike = false
                ),
                Post(
                    id_publicaciones = 2,
                    Id_usuario = 102,
                    Titulo = "Apuntes de matemáticas",
                    Descripcion = "¿Alguien tiene apuntes de la clase de matemáticas?",
                    Fecha_creacion = "2024-03-08",
                    Fecha_modificacion = "2024-03-08",
                    Borrador = 0,
                    usuarioAlias = "maria_123",
                    likes = 3,
                    comentarios = 4,
                    usuarioDioLike = false
                ),
                Post(
                    id_publicaciones = 3,
                    Id_usuario = 103,
                    Titulo = "Artículo de programación",
                    Descripcion = "Comparto este artículo interesante sobre programación en Android",
                    Fecha_creacion = "2024-03-07",
                    Fecha_modificacion = "2024-03-07",
                    Borrador = 0,
                    usuarioAlias = "carlos_tech",
                    likes = 10,
                    comentarios = 1,
                    usuarioDioLike = false
                )
            )
        )

        // Actualizar el adaptador con los nuevos datos
        postAdapter.updatePosts(postsList)
    }

    private fun manejarLike(post: Post) {
        val index = postsList.indexOfFirst { it.id_publicaciones == post.id_publicaciones }
        if (index != -1) {
            val postActualizado = post.copy(
                likes = if (post.usuarioDioLike) post.likes - 1 else post.likes + 1,
                usuarioDioLike = !post.usuarioDioLike
            )
            postsList[index] = postActualizado
            postAdapter.updatePosts(postsList)

            com.google.android.material.snackbar.Snackbar.make(
                requireView(),
                if (postActualizado.usuarioDioLike) "Te gusta esta publicación" else "Quitaste el like",
                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun abrirComentarios(post: Post) {
        com.google.android.material.snackbar.Snackbar.make(
            requireView(),
            "Comentarios de ${post.usuarioAlias}",
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun verPostDetalle(post: Post) {
        com.google.android.material.snackbar.Snackbar.make(
            requireView(),
            "Viendo: ${post.Titulo}",
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun mostrarDialogoNuevoPost() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_post, null)
        val etTitulo = dialogView.findViewById<TextInputEditText>(R.id.etNuevoPostTitulo)
        val etDescripcion = dialogView.findViewById<TextInputEditText>(R.id.etNuevoPostDescripcion)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nueva publicación")
            .setView(dialogView)
            .setPositiveButton("Publicar") { _, _ ->
                val titulo = etTitulo.text.toString().trim()
                val descripcion = etDescripcion.text.toString().trim()

                if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                    crearNuevoPost(titulo, descripcion)
                } else {
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Título y descripción son requeridos",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun crearNuevoPost(titulo: String, descripcion: String) {
        val nuevoPost = Post(
            id_publicaciones = (postsList.size + 1), // ID temporal
            Id_usuario = 101, // TODO: Obtener ID del usuario logueado
            Titulo = titulo,
            Descripcion = descripcion,
            Fecha_creacion = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date()),
            Fecha_modificacion = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .format(java.util.Date()),
            Borrador = 0,
            usuarioAlias = "usuario_actual", // TODO: Obtener alias del usuario logueado
            likes = 0,
            comentarios = 0,
            usuarioDioLike = false
        )

        postsList.add(0, nuevoPost)
        postAdapter.updatePosts(postsList)


        recyclerView.scrollToPosition(0)

        com.google.android.material.snackbar.Snackbar.make(
            requireView(),
            "Publicación creada",
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }
}