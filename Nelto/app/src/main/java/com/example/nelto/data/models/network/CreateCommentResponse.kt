package com.example.nelto.data.models.network

import com.example.nelto.models.Comment

data class CreateCommentResponse(
    val message: String,
    val comment: Comment
)