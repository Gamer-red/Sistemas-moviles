package com.example.nelto.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.example.nelto.R
import com.example.nelto.models.Post
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PostAdapter(
    private var posts: List<Post>,
    private val onLikeClick: (Post) -> Unit,
    private val onCommentClick: (Post) -> Unit,
    private val onPostClick: (Post) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    // ViewHolder: Contiene las vistas de cada item
    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivAvatar: ImageView = itemView.findViewById(R.id.ivPostAvatar)
        val tvAlias: TextView = itemView.findViewById(R.id.tvPostAlias)
        val tvFecha: TextView = itemView.findViewById(R.id.tvPostFecha)
        val tvContenido: TextView = itemView.findViewById(R.id.tvPostContenido)
        val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val btnLike: MaterialButton = itemView.findViewById(R.id.btnLike)
        val tvLikes: TextView = itemView.findViewById(R.id.tvLikes)
        val btnComment: MaterialButton = itemView.findViewById(R.id.btnComment)
        val tvComentarios: TextView = itemView.findViewById(R.id.tvComentarios)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        // Configurar datos básicos
        holder.tvAlias.text = "@${post.usuarioAlias}"
        holder.tvContenido.text = post.Descripcion
        holder.tvAlias.text = "@${post.usuarioAlias}"

        // Formatear fecha
        holder.tvFecha.text = post.Fecha_creacion

        // Configurar contadores
        holder.tvLikes.text = post.likes.toString()
        holder.tvComentarios.text = post.comentarios.toString()

        // Configurar estado del like
        if (post.usuarioDioLike) {
            holder.btnLike.setIconResource(android.R.drawable.btn_star_big_on)
            holder.btnLike.text = "Quitar like"
        } else {
            holder.btnLike.setIconResource(android.R.drawable.btn_star_big_off)
            holder.btnLike.text = "Me gusta"
        }

        // Click listeners
        holder.btnLike.setOnClickListener {
            onLikeClick(post)
        }

        holder.btnComment.setOnClickListener {
            onCommentClick(post)
        }

        holder.itemView.setOnClickListener {
            onPostClick(post)
        }
    }

    override fun getItemCount() = posts.size

    // Función para actualizar la lista
    fun updatePosts(newPosts: List<Post>) {
        this.posts = newPosts
        notifyDataSetChanged()
    }
}