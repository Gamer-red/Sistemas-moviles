package com.example.nelto.data.models.network

data class LikeResponse(
    val message: String,
    val action: String,  // "added", "updated", "removed"
    val userReaction: String?,
    val counts: LikeCounts
)

data class LikeCounts(
    val me_gusta: Int,
    val no_megusta: Int
)