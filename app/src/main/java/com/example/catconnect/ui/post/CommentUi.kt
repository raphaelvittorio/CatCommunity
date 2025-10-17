package com.example.catconnect.ui.post

/**
 * Menyimpan data komentar yang sudah siap untuk ditampilkan di UI.
 */
data class CommentUi(
    val id: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val text: String
)