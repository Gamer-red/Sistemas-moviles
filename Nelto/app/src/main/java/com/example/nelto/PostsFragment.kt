package com.example.nelto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nelto.adapters.PostAdapter
import com.example.nelto.models.Post
import com.example.nelto.data.repositories.PostRepository
import com.example.nelto.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PostsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var postRepository: PostRepository
    private lateinit var sessionManager: SessionManager

    private val postsList = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_posts, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewPosts)

        sessionManager = SessionManager(requireContext())
        postRepository = PostRepository(sessionManager)

        setupRecyclerView()
        cargarPublicaciones()

        return view
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(
            posts = postsList,
            onLikeClick = { post -> manejarLike(post) },
            onCommentClick = { post -> abrirComentarios(post) },
            onPostClick = { post -> verPostDetalle(post) },
            onGuardarClick = { post -> manejarGuardado(post) }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = postAdapter
        Log.d("PostsFragment", "Adaptador inicializado")
    }

    private fun cargarPublicaciones() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = postRepository.getPublicaciones(1, 20)

                withContext(Dispatchers.Main) {
                    if (response != null) {
                        postsList.clear()
                        postsList.addAll(response.posts)
                        postAdapter.updatePosts(postsList)
                        Log.d("PostsFragment", "✅ Mostrando ${postsList.size} publicaciones")
                    } else {
                        Log.e("PostsFragment", "❌ Error al cargar publicaciones")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("PostsFragment", "❌ Excepción: ${e.message}")
                }
            }
        }
    }

    private fun manejarLike(post: Post) {
        val userId = sessionManager.getUserId()
        Log.d("PostsFragment", "🔘 Click en like - PostId: ${post.id_publicaciones}, UserId: $userId")

        if (userId == 0) {
            Log.e("PostsFragment", "❌ Usuario no logueado")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("PostsFragment", "📡 Enviando petición de like al backend")
                val result = postRepository.toggleLike(post.id_publicaciones, userId)

                withContext(Dispatchers.Main) {
                    if (result != null) {
                        Log.d("PostsFragment", "✅ Respuesta: action=${result.action}, userReaction=${result.userReaction}")

                        val index = postsList.indexOfFirst { it.id_publicaciones == post.id_publicaciones }
                        if (index != -1) {
                            val postActualizado = when (result.action) {
                                "removed" -> {
                                    // Se quitó el like - actualizar contador y estado
                                    post.copy(
                                        likes = result.counts?.me_gusta ?: 0,
                                        usuarioDioLike = false
                                    )
                                }
                                else -> {
                                    // "added" o "updated" - actualizar normalmente
                                    post.copy(
                                        likes = result.counts?.me_gusta ?: 0,
                                        usuarioDioLike = result.userReaction == "me_gusta"
                                    )
                                }
                            }
                            postsList[index] = postActualizado
                            postAdapter.updatePosts(postsList)

                            // Mostrar mensaje opcional
                            val mensaje = when (result.action) {
                                "removed" -> "Like eliminado"
                                "added" -> "Te gusta esta publicación"
                                else -> "Like actualizado"
                            }
                            com.google.android.material.snackbar.Snackbar.make(
                                requireView(),
                                mensaje,
                                com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.e("PostsFragment", "❌ Resultado nulo")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("PostsFragment", "❌ Excepción: ${e.message}")
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Error al procesar like",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun abrirComentarios(post: Post) {
        Log.d("PostsFragment", "Abrir comentarios para: ${post.Titulo}")

        // Crear un diálogo simple para comentarios
        val dialogView = layoutInflater.inflate(R.layout.dialog_comentarios, null)
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialogComments)
        val etComentario = dialogView.findViewById<TextInputEditText>(R.id.etDialogComentario)
        val btnEnviar = dialogView.findViewById<MaterialButton>(R.id.btnDialogEnviar)

        // Configurar RecyclerView para comentarios
        val commentAdapter = CommentAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentAdapter

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Comentarios - ${post.Titulo}")
            .setView(dialogView)
            .setPositiveButton("Cerrar", null)
            .show()

        // Cargar comentarios existentes
        cargarComentariosEnDialog(post.id_publicaciones, commentAdapter)

        // Enviar nuevo comentario
        btnEnviar.setOnClickListener {
            val comentario = etComentario.text.toString().trim()
            if (comentario.isNotEmpty()) {
                enviarComentario(post.id_publicaciones, comentario, commentAdapter, etComentario)
            }
        }
    }

    private fun cargarComentariosEnDialog(postId: Int, adapter: CommentAdapter) {
        CoroutineScope(Dispatchers.IO).launch {
            val comments = postRepository.getCommentsByPost(postId)
            withContext(Dispatchers.Main) {
                adapter.updateComments(comments)
            }
        }
    }

    private fun enviarComentario(postId: Int, comentario: String, adapter: CommentAdapter, etComentario: TextInputEditText) {
        CoroutineScope(Dispatchers.IO).launch {
            val newComment = postRepository.createComment(postId, comentario)
            withContext(Dispatchers.Main) {
                if (newComment != null) {
                    etComentario.text?.clear()

                    // Recargar comentarios
                    val comments = postRepository.getCommentsByPost(postId)
                    adapter.updateComments(comments)

                    // ✅ ACTUALIZAR CONTADOR EN EL POST
                    actualizarContadorComentarios(postId, comments.size)

                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Comentario agregado",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Error al enviar comentario",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun actualizarContadorComentarios(postId: Int, nuevoTotal: Int) {
        val index = postsList.indexOfFirst { it.id_publicaciones == postId }
        if (index != -1) {
            val postActualizado = postsList[index].copy(commentsCount = nuevoTotal)
            postsList[index] = postActualizado
            postAdapter.updatePosts(postsList)
        }
    }

    private fun verPostDetalle(post: Post) {
        Log.d("PostsFragment", "Viendo: ${post.Titulo}")
        com.google.android.material.snackbar.Snackbar.make(
            requireView(),
            "Viendo: ${post.Titulo}",
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun manejarGuardado(post: Post) {
        Log.d("PostsFragment", "Guardar: ${post.Titulo}")
        com.google.android.material.snackbar.Snackbar.make(
            requireView(),
            "Funcionalidad pendiente: Guardar",
            com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
        ).show()
    }
}