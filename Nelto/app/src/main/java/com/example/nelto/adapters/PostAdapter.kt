package com.example.nelto.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nelto.R
import com.example.nelto.models.Post
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale

class PostAdapter(
    private var posts: List<Post>,
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit,
    private val onGuardarClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlias: TextView = itemView.findViewById(R.id.tvPostAlias)
        val tvFecha: TextView = itemView.findViewById(R.id.tvPostFecha)
        val tvTitulo: TextView = itemView.findViewById(R.id.tvPostTitulo)
        val tvContenido: TextView = itemView.findViewById(R.id.tvPostContenido)
        val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        val tvComentarios: TextView = itemView.findViewById(R.id.tvComentarios)
        val btnLike: MaterialButton = itemView.findViewById(R.id.btnLike)  // ← AGREGAR ESTO

        val btnComment: MaterialButton = itemView.findViewById(R.id.btnComment)  // ← AGREGAR ESTO
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        holder.tvAlias.text = "@${post.Alias}"
        holder.tvTitulo.text = post.Titulo
        holder.tvContenido.text = post.Descripcion
        holder.tvLikes.text = post.likes.toString()
        holder.tvComentarios.text = post.commentsCount.toString()
        holder.btnComment.setOnClickListener {
            onCommentClick(post)
        }
        // Formatear fecha
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = inputFormat.parse(post.Fecha_creacion)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            holder.tvFecha.text = outputFormat.format(date)
        } catch (e: Exception) {
            holder.tvFecha.text = post.Fecha_creacion
        }

        // Configurar botón like según el estado
        if (post.usuarioDioLike) {
            holder.btnLike.setIconResource(android.R.drawable.btn_star_big_on)
        } else {
            holder.btnLike.setIconResource(android.R.drawable.btn_star_big_off)
        }
        holder.btnLike.text = "Me gusta"

        // Click listener para el botón like
        holder.btnLike.setOnClickListener {
            onLikeClick(post)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }
}