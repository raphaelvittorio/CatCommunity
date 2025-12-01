package com.example.catconnect.data.model

/**
 * Data class representing a Post object ready to be displayed on the UI.
 * It might contain formatted text or combined data from other models.
 */
data class PostUi(
    val id: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val photoUrl: String,
    val caption: String,
    val likes: Int,
    val isLiked: Boolean,
    val isSaved: Boolean
)
