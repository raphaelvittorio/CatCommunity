//package com.example.catconnect.ui.post
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.switchMap
//import com.example.catconnect.data.model.Post
//import com.example.catconnect.data.model.Comment
//import com.example.catconnect.data.model.User
//import com.example.catconnect.data.repo.FakeRepository
//
//class PostDetailViewModel : ViewModel() {
//
//    private val _postId = MutableLiveData<String>()
//
//    // Expose the raw Post LiveData
//    val post: LiveData<Post?> = _postId.switchMap { id ->
//        FakeRepository.getPost(id)
//    }
//
//    // Expose the raw Comment List LiveData
//    val comments: LiveData<List<Comment>> = _postId.switchMap { id ->
//        FakeRepository.commentsFor(id)
//    }
//
//    // Expose the current user
//    val currentUser: LiveData<User> = FakeRepository.currentUser
//
//    fun setPostId(postId: String) {
//        if (_postId.value != postId) {
//            _postId.value = postId
//        }
//    }
//
//    fun addComment(text: String) {
//        val postId = _postId.value ?: return
//        // The repository will handle adding the comment and the LiveData will update.
//        FakeRepository.addComment(postId, text)
//    }
//
//    fun toggleLike() {
//        val currentPost = post.value ?: return
//        val user = currentUser.value ?: return
//        // The repository will handle the like toggle and the LiveData will update.
//        FakeRepository.toggleLike(currentPost, user.id)
//    }
//}
