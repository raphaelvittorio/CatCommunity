package com.example.catconnect.data.model

data class Post(
    val id: String,
    val userId: String,
    val photoUrl: String,
    val caption: String,
    val breed: String? = null,
    val ageMonth: Int = 0,
    val likes: Int = 0,

    val isLiked: Boolean = false,
    val isSaved: Boolean = false
)
