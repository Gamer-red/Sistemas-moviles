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
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import android.widget.LinearLayout

class ProfileFragment : Fragment() {

    private lateinit var ivAvatar: ImageView
    private lateinit var tvNombre: TextView
    private lateinit var tvAlias: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefono: TextView
    private lateinit var btnEditarPerfil: MaterialButton
    private lateinit var btnMisPublicaciones: MaterialButton  // NUEVO

    private lateinit var btnPublicacionesGuardadas: MaterialButton

    private lateinit var postAdapter: PostAdapter
    private val publicacionesEjemplo = mutableListOf<Post>()
    private val borradoresEjemplo = mutableListOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ProfileFragment", "onCreateView iniciado")
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        Log.d("ProfileFragment", "Layout inflado: $view")

        initViews(view)
        cargarDatosEjemplo()
        setupListeners()

        Log.d("ProfileFragment", "onCreateView completado")
        return view
    }

    private fun initViews(view: View) {
        Log.d("ProfileFragment", "Inicializando vistas")

        ivAvatar = view.findViewById(R.id.ivProfileAvatar)
        tvNombre = view.findViewById(R.id.tvProfileNombre)
        tvAlias = view.findViewById(R.id.tvProfileAlias)
        tvEmail = view.findViewById(R.id.tvProfileEmail)
        tvTelefono = view.findViewById(R.id.tvProfileTelefono)
        btnEditarPerfil = view.findViewById(R.id.btnEditarPerfil)
        btnMisPublicaciones = view.findViewById(R.id.btnMisPublicaciones)  // NUEVO
        btnPublicacionesGuardadas = view.findViewById(R.id.btnPublicacionesGuardadas)
        Log.d("ProfileFragment", "Todas las vistas inicializadas")
    }

    private fun cargarDatosEjemplo() {
        // Datos de ejemplo estáticos para mostrar en UI
        tvNombre.text = "Juan Pérez García"
        tvAlias.text = "@juanito"
        tvEmail.text = "juan.perez@ejemplo.com"
        tvTelefono.text = "555-123-4567"

        // Crear publicaciones de ejemplo
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
                    Descripcion = "Este es un post que aún no he publicado.",
                    Fecha_creacion = "2024-03-06",
                    Fecha_modificacion = "2024-03-06",
                    Borrador = 1,
                    usuarioAlias = "juanito",
                    likes = 0,
                    comentarios = 0,
                    usuarioDioLike = false
                )
            )
        )
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

        btnPublicacionesGuardadas.setOnClickListener {
            mostrarPublicacionesGuardadas()
        }

        // NUEVO: Listener para el botón Mis Publicaciones
        btnMisPublicaciones.setOnClickListener {
            mostrarDialogoMisPublicaciones()
        }
    }

    // NUEVO: Función para mostrar diálogo con opciones
    private fun mostrarDialogoMisPublicaciones() {
        val opciones = arrayOf("Ver publicaciones publicadas", "Ver borradores")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mis publicaciones")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarPublicacionesPublicadas()
                    1 -> mostrarBorradores()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVO: Mostrar publicaciones publicadas
    private fun mostrarPublicacionesPublicadas() {
        if (publicacionesEjemplo.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sin publicaciones")
                .setMessage("Aún no has publicado nada. ¡Crea tu primera publicación!")
                .setPositiveButton("Aceptar", null)
                .show()
        } else {
            val dialogView = layoutInflater.inflate(R.layout.dialog_lista_posts, null)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialog)

            val adapter = PostAdapter(
                posts = publicacionesEjemplo,
                onLikeClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Like en: ${post.Titulo}",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onCommentClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Comentarios de: ${post.Titulo}",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onPostClick = { post ->
                    // En lugar de solo ver detalle, mostramos opciones
                    mostrarOpcionesPublicacion(post)
                },
                onGuardarClick = { post ->  // ← NUEVO PARÁMETRO AGREGADO
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "No puedes guardar tus propias publicaciones",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Mis publicaciones")
                .setView(dialogView)
                .setPositiveButton("Cerrar", null)
                .show()
        }
    }

    // NUEVO: Mostrar borradores
    private fun mostrarBorradores() {
        if (borradoresEjemplo.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sin borradores")
                .setMessage("No tienes borradores guardados.")
                .setPositiveButton("Aceptar", null)
                .show()
        } else {
            val dialogView = layoutInflater.inflate(R.layout.dialog_lista_posts, null)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialog)

            val adapter = PostAdapter(
                posts = borradoresEjemplo,
                onLikeClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "No puedes dar like a un borrador",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onCommentClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "No puedes comentar en un borrador",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onPostClick = { post ->
                    // Mostrar opciones para borrador
                    mostrarOpcionesBorrador(post)
                },
                onGuardarClick = { post ->  // ← PARÁMETRO AGREGADO
                    // Aquí sí tiene sentido: permitir quitar de guardados
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Quitar de guardados: ${post.Titulo}",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Mis borradores")
                .setView(dialogView)
                .setPositiveButton("Cerrar", null)
                .show()
        }
    }

    private fun mostrarOpcionesPublicacion(post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_opciones_post, null)

        val tvTitulo = dialogView.findViewById<TextView>(R.id.tvPostTitulo)
        val layoutPublicado = dialogView.findViewById<LinearLayout>(R.id.layoutOpcionesPublicado)
        val layoutBorrador = dialogView.findViewById<LinearLayout>(R.id.layoutOpcionesBorrador)
        val btnEditar = dialogView.findViewById<MaterialButton>(R.id.btnEditarPost)
        val btnEliminar = dialogView.findViewById<MaterialButton>(R.id.btnEliminarPost)
        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btnCancelar)

        tvTitulo.text = post.Titulo
        layoutPublicado.visibility = View.VISIBLE
        layoutBorrador.visibility = View.GONE

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .show()

        btnEditar.setOnClickListener {
            dialog.dismiss()
            editarPublicacion(post)
        }

        btnEliminar.setOnClickListener {
            dialog.dismiss()
            confirmarEliminarPublicacion(post)
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    // NUEVO: Mostrar diálogo con opciones para borrador
    private fun mostrarOpcionesBorrador(post: Post) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_opciones_post, null)

        val tvTitulo = dialogView.findViewById<TextView>(R.id.tvPostTitulo)
        val layoutPublicado = dialogView.findViewById<LinearLayout>(R.id.layoutOpcionesPublicado)
        val layoutBorrador = dialogView.findViewById<LinearLayout>(R.id.layoutOpcionesBorrador)
        val btnPublicar = dialogView.findViewById<MaterialButton>(R.id.btnPublicarAhora)
        val btnEditar = dialogView.findViewById<MaterialButton>(R.id.btnEditarBorrador)
        val btnEliminar = dialogView.findViewById<MaterialButton>(R.id.btnEliminarBorrador)
        val btnCancelar = dialogView.findViewById<MaterialButton>(R.id.btnCancelar)

        tvTitulo.text = post.Titulo
        layoutPublicado.visibility = View.GONE
        layoutBorrador.visibility = View.VISIBLE

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .show()

        btnPublicar.setOnClickListener {
            dialog.dismiss()
            publicarBorrador(post)
        }

        btnEditar.setOnClickListener {
            dialog.dismiss()
            editarBorrador(post)
        }

        btnEliminar.setOnClickListener {
            dialog.dismiss()
            confirmarEliminarBorrador(post)
        }

        btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
    }

    // NUEVO: Editar publicación
    private fun editarPublicacion(post: Post) {
        // Aquí irá la lógica para editar publicación
        mostrarDialogoEditarPost(post, false) // false = no es borrador
    }

    // NUEVO: Editar borrador
    private fun editarBorrador(post: Post) {
        // Aquí irá la lógica para editar borrador
        mostrarDialogoEditarPost(post, true) // true = es borrador
    }

    // NUEVO: Publicar borrador
    private fun publicarBorrador(post: Post) {
        // Aquí irá la lógica para publicar el borrador
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Publicar borrador")
            .setMessage("¿Estás seguro de que quieres publicar este borrador?")
            .setPositiveButton("Sí, publicar") { _, _ ->
                // Simular publicación
                val index = borradoresEjemplo.indexOfFirst { it.id_publicaciones == post.id_publicaciones }
                if (index != -1) {
                    val postPublicado = post.copy(
                        Borrador = 0,

                    )
                    borradoresEjemplo.removeAt(index)
                    publicacionesEjemplo.add(0, postPublicado)

                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "¡Publicación publicada exitosamente!",
                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVO: Confirmar eliminar publicación
    private fun confirmarEliminarPublicacion(post: Post) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar publicación")
            .setMessage("¿Estás seguro de que quieres eliminar esta publicación? Esta acción no se puede deshacer.")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                // Aquí irá la lógica para eliminar
                publicacionesEjemplo.removeAll { it.id_publicaciones == post.id_publicaciones }
                com.google.android.material.snackbar.Snackbar.make(
                    requireView(),
                    "Publicación eliminada",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVO: Confirmar eliminar borrador
    private fun confirmarEliminarBorrador(post: Post) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar borrador")
            .setMessage("¿Estás seguro de que quieres eliminar este borrador?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                // Aquí irá la lógica para eliminar
                borradoresEjemplo.removeAll { it.id_publicaciones == post.id_publicaciones }
                com.google.android.material.snackbar.Snackbar.make(
                    requireView(),
                    "Borrador eliminado",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    // NUEVO: Diálogo para editar post
    private fun mostrarDialogoEditarPost(post: Post, esBorrador: Boolean) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_nuevo_post, null)

        val etTitulo = dialogView.findViewById<TextInputEditText>(R.id.etNuevoPostTitulo)
        val etDescripcion = dialogView.findViewById<TextInputEditText>(R.id.etNuevoPostDescripcion)

        // Cargar datos existentes
        etTitulo.setText(post.Titulo)
        etDescripcion.setText(post.Descripcion)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (esBorrador) "Editar borrador" else "Editar publicación")
            .setView(dialogView)
            .setPositiveButton("Guardar", null) // Lo configuraremos después
            .setNegativeButton("Cancelar", null)
            .show()

        // Configurar el botón positivo para evitar que se cierre automáticamente
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val nuevoTitulo = etTitulo.text.toString().trim()
            val nuevaDescripcion = etDescripcion.text.toString().trim()

            if (nuevoTitulo.isEmpty() || nuevaDescripcion.isEmpty()) {
                com.google.android.material.snackbar.Snackbar.make(
                    requireView(),
                    "Título y descripción son requeridos",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Actualizar el post
            val index = if (esBorrador) {
                borradoresEjemplo.indexOfFirst { it.id_publicaciones == post.id_publicaciones }
            } else {
                publicacionesEjemplo.indexOfFirst { it.id_publicaciones == post.id_publicaciones }
            }

            if (index != -1) {
                val postActualizado = post.copy(
                    Titulo = nuevoTitulo,
                    Descripcion = nuevaDescripcion,
                    Fecha_modificacion = "2024-03-15"
                )

                if (esBorrador) {
                    borradoresEjemplo[index] = postActualizado
                } else {
                    publicacionesEjemplo[index] = postActualizado
                }

                com.google.android.material.snackbar.Snackbar.make(
                    requireView(),
                    if (esBorrador) "Borrador actualizado" else "Publicación actualizada",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()

                dialog.dismiss()
            }
        }
    }

    private fun mostrarPublicacionesGuardadas() {
        // Obtener las publicaciones guardadas desde PostsFragment
        // Por ahora usamos datos de ejemplo
        val guardadasEjemplo = listOf(
            Post(
                id_publicaciones = 10,
                Id_usuario = 200,
                Titulo = "Post guardado 1",
                Descripcion = "Este es un ejemplo de publicación guardada",
                Fecha_creacion = "2024-03-15",
                Fecha_modificacion = "2024-03-15",
                Borrador = 0,
                usuarioAlias = "otro_usuario",
                likes = 5,
                comentarios = 2,
                usuarioDioLike = false,
                guardado = true
            ),
            Post(
                id_publicaciones = 11,
                Id_usuario = 201,
                Titulo = "Post guardado 2",
                Descripcion = "Otro ejemplo de publicación guardada",
                Fecha_creacion = "2024-03-14",
                Fecha_modificacion = "2024-03-14",
                Borrador = 0,
                usuarioAlias = "usuario_amigo",
                likes = 3,
                comentarios = 1,
                usuarioDioLike = false,
                guardado = true
            )
        )

        if (guardadasEjemplo.isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Sin publicaciones guardadas")
                .setMessage("No tienes publicaciones guardadas. Ve al muro principal y guarda algunas.")
                .setPositiveButton("Aceptar", null)
                .show()
        } else {
            val dialogView = layoutInflater.inflate(R.layout.dialog_lista_posts, null)
            val recyclerView = dialogView.findViewById<RecyclerView>(R.id.recyclerViewDialog)

            val adapter = PostAdapter(
                posts = guardadasEjemplo,
                onLikeClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Like en publicación guardada",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onCommentClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Comentarios de publicación guardada",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onPostClick = { post ->
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Viendo: ${post.Titulo}",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                },
                onGuardarClick = { post ->
                    // Opcional: permitir quitar de guardados desde aquí
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Funcionalidad: Quitar de guardados",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = adapter

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Mis publicaciones guardadas")
                .setView(dialogView)
                .setPositiveButton("Cerrar", null)
                .show()
        }
    }
}