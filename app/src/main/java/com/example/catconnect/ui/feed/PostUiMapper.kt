package com.example.catconnect.ui.mappers

import com.example.catconnect.data.model.Post
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.ui.feed.PostUi

fun Post.toUi(): PostUi {
    // Ambil user dari repository
    val user = FakeRepository.getUser(userId)

    return if (user != null) {
        // KASUS NORMAL: User ditemukan, gunakan datanya
        PostUi(
            id = id,
            photoUrl = photoUrl,
            authorName = user.name, // Data dari user yang ditemukan
            breed = breed,
            ageMonth = ageMonth,
            caption = caption,
            likes = likes,
            isLiked = isLiked, // Gunakan properti isLiked dari Post
            isSaved = isSaved, // Gunakan properti isSaved dari Post
            authorPhotoUrl = user.photoUrl // Data dari user yang ditemukan
        )
    } else {
        // KASUS PENGAMAN: User tidak ditemukan
        PostUi(
            id = id,
            photoUrl = photoUrl,
            authorName = "Unknown User", // Tampilkan nama default
            breed = breed,
            ageMonth = ageMonth,
            caption = caption,
            likes = likes,
            isLiked = isLiked,
            isSaved = isSaved,
            authorPhotoUrl = null // Tidak ada foto profil
        )
    }
}
