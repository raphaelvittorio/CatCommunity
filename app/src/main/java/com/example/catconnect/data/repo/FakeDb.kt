package com.example.catconnect.data.repo

import com.example.catconnect.data.model.*
import com.example.catconnect.data.model.Adoption

object FakeDb {
    // === USER AKTIF (sesuaikan dgn modelmu: id, name, email, password, photoUrl, bio) ===
    var currentUser = User(
        "u1",
        "Alif",
        "alif@email.com",
        "password123",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSDUw51i6ZRy2Lxx57KeJxgsOrNbfapDRr4bA&s",
        "Cat lover"
    )

    // === KOLEKSI DUMMY (SAMAKAN DENGAN MODEL SAAT INI) ===
    val users = mutableListOf(
        currentUser,
        User("u2", "Bela", "bela@email.com", "password123", "https://cataas.com/cat/says/Bela?width=201&height=201", "Meow enthusiast"),
        User("u3", "Billy", "billy@email.com", "password123", "https://cataas.com/cat/says/Charlie?width=202&height=202", "Crazy cat person"),
        User("u4", "Ucok", "ucok@email.com", "password123", "https://cataas.com/cat/says/Dian?width=203&height=203", "Kitten cuddler")
    )

    // Post constructor: (id, userId, photoUrl, caption, breed, ageMonth, likes)
    val posts = mutableListOf(
        Post("p1", "u1", "https://cataas.com/cat?width=400&height=400&unique=p1", "Do something today that your future self will thank you for.", "Persian", 12, 10),
        Post("p2", "u2", "https://cataas.com/cat?width=400&height=300&unique=p2", "You are my sunshine.", "Siamese", 5, 25),
        Post("p3", "u3", "https://cataas.com/cat?width=400&height=301&unique=p3", "Just chillin'.", "Maine Coon", 24, 50),
        Post("p4", "u4", "https://cataas.com/cat?width=400&height=302&unique=p4", "Looking for a forever home!", "Domestic Shorthair", 8, 15),
        Post("p5", "u1", "https://cataas.com/cat?width=400&height=303&unique=p5", "Sleepy head.", "Ragdoll", 18, 30),
        Post("p6", "u2", "https://cataas.com/cat?width=400&height=304&unique=p6", "Playtime is the best time.", "Bengal", 6, 42),
        Post("p7", "u3", "https://cataas.com/cat?width=400&height=305&unique=p7", "Curiosity killed the cat, but satisfaction brought it back.", "Sphynx", 36, 100),
        Post("p8", "u4", "https://cataas.com/cat?width=400&height=306&unique=p8", "Tiny but mighty.", "Munchkin", 4, 22),
        Post("p9", "u1", "https://cataas.com/cat?width=400&height=310&unique=p9", "Tiny but mighty.", "Munchkin", 4, 22),

    )


    // Koleksi lain siap dipakai fitur berikutnya (sesuaikan model yg sudah kamu buat)
    val comments = mutableListOf<Comment>()
    val events = mutableListOf<Event>()
    val adoptions = mutableListOf(
        Adoption("a1", "Milo", "https://cataas.com/cat?width=300&height=300&unique=a1", "Domestic Shorthair", 6),
        Adoption("a2", "Luna", "https://cataas.com/cat?width=300&height=300&unique=a2", "Siamese", 12),
        Adoption("a3", "Ollie", "https://cataas.com/cat?width=300&height=300&unique=a3", "Bengal", 8),
        Adoption("a4", "Simba", "https://cataas.com/cat?width=300&height=300&unique=a4", "Maine Coon", 24),
        Adoption("a5", "Nala", "https://cataas.com/cat?width=300&height=300&unique=a5", "Ragdoll", 18)
    )
    val rooms = mutableListOf<Room>()
    val messages = mutableListOf<Message>()
    val likedByUser = mutableSetOf<String>() // jejak like post oleh user aktif
}
