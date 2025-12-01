package com.example.catconnect.ui.chat

data class Conversation(
    val id: String,
    val userName: String,
    val lastMessage: String,
    val userImageUrl: String
)