package com.example.nelto.data.models.network

import com.example.nelto.models.Post

data class PostsResponse(
    val posts: List<Post>,
    val pagination: PaginationInfo
)

data class PaginationInfo(
    val page: Int,
    val limit: Int,
    val total: Int,
    val totalPages: Int
)