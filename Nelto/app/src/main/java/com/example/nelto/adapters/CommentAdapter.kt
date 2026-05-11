package com.example.nelto

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nelto.models.Comment

class CommentAdapter(
    private var comments: MutableList<Comment>
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAlias: TextView = itemView.findViewById(R.id.tvCommentAlias)
        val tvComentario: TextView = itemView.findViewById(R.id.tvCommentTexto)
        val tvFecha: TextView = itemView.findViewById(R.id.tvCommentFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment_dialog, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.tvAlias.text = "@${comment.Alias}"
        holder.tvComentario.text = comment.Comentario
        holder.tvFecha.text = "${comment.Fecha} ${comment.Hora}"
    }

    override fun getItemCount() = comments.size

    fun updateComments(newComments: List<Comment>) {
        comments.clear()
        comments.addAll(newComments)
        notifyDataSetChanged()
    }
}