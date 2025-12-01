package com.example.catconnect.ui.feed

data class CommentUi(
    val id: String,
    val postId: String,
    val authorId: String,
    val authorName: String?,
    val authorPhotoUrl: String?,
    val comment: String,
    val timestamp: Long
)
