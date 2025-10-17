package com.example.catconnect.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.catconnect.data.model.Post
import com.example.catconnect.data.repo.FakeRepository

class FeedViewModel : ViewModel() {
    // --- PERBAIKAN 1: 'getPosts' ---
    // Mengambil LiveData langsung dari properti 'posts' di Repository, bukan fungsi.
    val posts: LiveData<List<Post>> = FakeRepository.posts

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query
    fun setQuery(q: String) { _query.value = q }

    // Logika MediatorLiveData untuk filter sudah benar.
    val filteredPosts: LiveData<List<Post>> = MediatorLiveData<List<Post>>().apply {
        fun recompute() {
            val q = _query.value.orEmpty().trim().lowercase()
            val src = posts.value.orEmpty()
            value = if (q.isBlank()) {
                src
            } else {
                // Logika filter berdasarkan caption, breed, dan nama author.
                src.filter { p ->
                    val user = FakeRepository.getUser(p.userId)
                    (user?.name?.lowercase()?.contains(q) ?: false) ||
                            (p.breed?.lowercase()?.contains(q) ?: false) ||
                            (p.caption.lowercase().contains(q))
                }
            }
        }
        addSource(posts) { recompute() }
        addSource(_query) { recompute() }
    }

    fun toggleLike(postId: String) {
        // Cari post di dalam repository
        val post = FakeRepository.getPost(postId) ?: return
        // Panggil fungsi toggleLike di repository.
        // LiveData akan otomatis update karena data di repository berubah.
        FakeRepository.toggleLike(post)
    }

    // --- PERBAIKAN 2: 'toggleSave' ---
    fun toggleSave(postId: String) {
        // Cari post di dalam repository
        val post = FakeRepository.getPost(postId) ?: return
        // Panggil fungsi yang benar di repository, kemungkinan bernama 'toggleSaveStatus' atau serupa
        FakeRepository.toggleSave(post) // Mengasumsikan ada fungsi ini
    }
}
