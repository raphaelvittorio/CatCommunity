package com.example.catconnect.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.catconnect.data.model.Post
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.ui.feed.PostUi
import com.example.catconnect.ui.mappers.toUi

class ProfileViewModel : ViewModel() {
    val user = FakeRepository.currentUser

    // To handle the "undo" for delete
    private var lastDeletedPost: Post? = null

    // Map the LiveData<List<Post>> to LiveData<List<PostUi>>
    val myPosts = FakeRepository.posts.map { list: List<Post> ->
        list.filter { post -> post.userId == user.id }
            .map { it.toUi() } // Map each Post to PostUi
    }

    // Helper to find the original Post from a PostUi
    private fun findOriginalPost(postUi: PostUi): Post? {
        // Access the current value of the Flow/LiveData to find the match
        return FakeRepository.posts.value?.find { it.id == postUi.id }
    }

    fun likePost(post: PostUi) {
        val originalPost = findOriginalPost(post) ?: return
        val updated = originalPost.copy(
            likes = originalPost.likes + 1,
            isLiked = true
        )
        FakeRepository.updatePost(updated)
    }

    fun unlikePost(post: PostUi) {
        val originalPost = findOriginalPost(post) ?: return
        val updated = originalPost.copy(
            likes = originalPost.likes - 1,
            isLiked = false
        )
        FakeRepository.updatePost(updated)
    }

    fun deletePost(post: PostUi) {
        val originalPost = findOriginalPost(post)
        // Save the post before deleting, for the undo action
        lastDeletedPost = originalPost
        if (originalPost != null) {
            FakeRepository.deletePost(originalPost.id)
        }
    }

    // addPost no longer needs a parameter, it uses the saved lastDeletedPost
    fun addPost() {
        lastDeletedPost?.let {
            FakeRepository.addPost(it)
            lastDeletedPost = null // Clear after adding back
        }
    }
}
