package com.example.nelto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nelto.data.repositories.PostRepository
import com.example.nelto.models.Comment
import com.example.nelto.utils.SessionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class CommentsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etComentario: TextInputEditText
    private lateinit var btnEnviar: MaterialButton
    private lateinit var tvNoComentarios: TextView

    private lateinit var postRepository: PostRepository
    private lateinit var sessionManager: SessionManager
    private lateinit var commentAdapter: CommentAdapter

    private val commentsList = mutableListOf<Comment>()
    private var postId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = arguments?.getInt("postId") ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comments, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewComments)
        etComentario = view.findViewById(R.id.etComentario)
        btnEnviar = view.findViewById(R.id.btnEnviarComentario)
        tvNoComentarios = view.findViewById(R.id.tvNoComentarios)

        sessionManager = SessionManager(requireContext())
        postRepository = PostRepository(sessionManager)

        setupRecyclerView()
        cargarComentarios()
        setupListeners()

        return view
    }

    private fun setupRecyclerView() {
        commentAdapter = CommentAdapter(commentsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentAdapter
    }

    private fun cargarComentarios() {
        if (postId == 0) return

        CoroutineScope(Dispatchers.IO).launch {
            val comments = postRepository.getCommentsByPost(postId)

            withContext(Dispatchers.Main) {
                commentsList.clear()
                commentsList.addAll(comments)
                commentAdapter.updateComments(commentsList)

                if (commentsList.isEmpty()) {
                    tvNoComentarios.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvNoComentarios.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupListeners() {
        btnEnviar.setOnClickListener {
            val comentario = etComentario.text.toString().trim()
            if (comentario.isNotEmpty()) {
                enviarComentario(comentario)
            }
        }
    }

    private fun enviarComentario(comentario: String) {
        btnEnviar.isEnabled = false

        CoroutineScope(Dispatchers.IO).launch {
            val newComment = postRepository.createComment(postId, comentario)

            withContext(Dispatchers.Main) {
                btnEnviar.isEnabled = true

                if (newComment != null) {
                    etComentario.text?.clear()
                    commentsList.add(0, newComment)
                    commentAdapter.updateComments(commentsList)

                    tvNoComentarios.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    recyclerView.scrollToPosition(0)

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

    companion object {
        fun newInstance(postId: Int): CommentsFragment {
            val fragment = CommentsFragment()
            val args = Bundle()
            args.putInt("postId", postId)
            fragment.arguments = args
            return fragment
        }
    }
}