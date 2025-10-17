package com.example.catconnect.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catconnect.data.model.Post
import com.example.catconnect.data.model.*
import com.example.catconnect.data.model.Report
import com.example.catconnect.data.model.ReportType

object FakeRepository {
    private val _users = MutableLiveData(FakeDb.users.toList())
    private val _posts = MutableLiveData<List<Post>>(FakeDb.posts)

    private val _comments = MutableLiveData(FakeDb.comments.toList())
    private val _events = MutableLiveData(FakeDb.events.toList())
    private val _adoptions = MutableLiveData(FakeDb.adoptions.toList())
    private val _rooms = MutableLiveData(FakeDb.rooms.toList())
    private val _messages = MutableLiveData(FakeDb.messages.filter { it.roomId == "r1" })
    private val savedByUser = mutableSetOf<String>() // Set to track saved posts

    val currentUser get() = FakeDb.currentUser
    val users: LiveData<List<User>> = _users
    val posts: LiveData<List<Post>> = _posts
    val events: LiveData<List<Event>> = _events
    val adoptions: LiveData<List<Adoption>> = _adoptions

    // ==== POST & LIKE/COMMENT/SAVE ====
    fun addPost(p: Post) {
        val newList = _posts.value.orEmpty().toMutableList()
        newList.add(0, p)
        _posts.value = newList
    }

    fun updatePost(p: Post) {
        val currentList = _posts.value.orEmpty()
        val index = currentList.indexOfFirst { it.id == p.id }
        if (index != -1) {
            val newList = currentList.toMutableList()
            newList[index] = p
            _posts.value = newList
        }
    }

    fun deletePost(id: String) {
        val newList = _posts.value.orEmpty().toMutableList()
        newList.removeAll { it.id == id }
        _posts.value = newList
    }

    fun getPost(id: String): Post? {
        // Cari post langsung dari value LiveData saat ini
        return _posts.value?.firstOrNull { it.id == id }
    }


    fun isLiked(id: String) = FakeDb.likedByUser.contains(id)
    fun isSaved(id: String) = savedByUser.contains(id)

    fun toggleLike(post: Post): Post {
        val liked = FakeDb.likedByUser.contains(post.id)
        val updated = if (liked) {
            FakeDb.likedByUser.remove(post.id); post.copy(likes = maxOf(0, post.likes - 1))
        } else {
            FakeDb.likedByUser.add(post.id); post.copy(likes = post.likes + 1)
        }
        updatePost(updated)
        return updated
    }

    fun toggleSave(post: Post) {
        val updatedPost = post.copy(isSaved = !post.isSaved)
        val currentList = _posts.value.orEmpty()
        val index = currentList.indexOfFirst { it.id == post.id }
        if (index != -1) {
            val newList = currentList.toMutableList()
            newList[index] = updatedPost
            _posts.value = newList
        }
    }

    fun toggleSaveStatus(postId: String) {
        if (savedByUser.contains(postId)) {
            savedByUser.remove(postId)
        } else {
            savedByUser.add(postId)
        }
    }

    // ==== USER ====
    fun updateUser(u: User) { FakeDb.users.replace(u) { it.id==u.id }; _users.value = FakeDb.users.toList() }
    fun findUsers(keyword: String) = FakeDb.users.filter {
        it.name.contains(keyword, ignoreCase = true) ||
                it.bio.contains(keyword, ignoreCase = true)
    }

    // ==== EVENTS ====
    fun upsertEvent(e: Event) { FakeDb.events.replace(e){it.id==e.id} ?: FakeDb.events.add(e); _events.value = FakeDb.events.toList() }
    fun deleteEvent(id: String) { FakeDb.events.removeAll { it.id==id }; _events.value = FakeDb.events.toList() }

    // ==== ADOPTION ====
    fun upsertAdoption(a: Adoption) { FakeDb.adoptions.replace(a){it.id==a.id} ?: FakeDb.adoptions.add(a); _adoptions.value = FakeDb.adoptions.toList() }
    fun updateAdoptionStatus(id: String, st: AdoptionStatus) { FakeDb.adoptions.find{it.id==id}?.apply{status=st}; _adoptions.value = FakeDb.adoptions.toList() }

    // ==== CHAT (dummy realtime) ====
    fun roomMessages(roomId: String): LiveData<List<Message>> =
        MutableLiveData(FakeDb.messages.filter { it.roomId==roomId }.sortedBy { it.createdAt })
    fun sendMessage(roomId: String, text: String) {
        val msg = Message("m"+System.currentTimeMillis(), roomId, currentUser.id, text, System.currentTimeMillis())
        FakeDb.messages.add(msg); _messages.value = FakeDb.messages.filter { it.roomId==roomId }
    }

    // ---- KOMENTAR (stream per-post) ----
    private val commentStreams = mutableMapOf<String, MutableLiveData<List<Comment>>>()

    fun commentsFor(postId: String): LiveData<List<Comment>> {
        return commentStreams.getOrPut(postId) {
            MutableLiveData(FakeDb.comments.filter { it.postId == postId }.sortedBy { it.createdAt })
        }
    }

    private fun pushComments(postId: String) {
        commentStreams[postId]?.value =
            FakeDb.comments.filter { it.postId == postId }.sortedBy { it.createdAt }
    }

    fun addComment(postId: String, text: String) {
        val c = Comment(
            id = "c" + System.currentTimeMillis(),
            postId = postId,
            userId = FakeDb.currentUser.id,
            text = text,
            createdAt = System.currentTimeMillis()
        )
        FakeDb.comments.add(c)
        pushComments(c.postId)
    }

    fun deleteComment(commentId: String) {
        val c = FakeDb.comments.firstOrNull { it.id == commentId } ?: return
        FakeDb.comments.remove(c)
        pushComments(c.postId)
    }

    // helper kecil
    fun getUser(userId: String): User? = FakeDb.users.firstOrNull { it.id == userId }

    // ==== REPORT ====
    private val _reports = MutableLiveData<List<Report>>(emptyList())
    val reports: LiveData<List<Report>> = _reports

    fun hasReported(targetId: String, userId: String): Boolean =
        _reports.value.orEmpty().any { it.targetId == targetId && it.reporterId == userId }

    fun addReport(
        targetId: String,
        reporterId: String,
        reason: String,
        type: ReportType = ReportType.POST
    ) {
        if (hasReported(targetId, reporterId)) return
        val r = Report(
            id = "r" + System.currentTimeMillis(),
            type = type,
            targetId = targetId,
            reporterId = reporterId,
            reason = reason,
            createdAt = System.currentTimeMillis()
        )
        _reports.value = _reports.value.orEmpty() + r
    }

}

// util replace or add
private inline fun <T> MutableList<T>.replace(newItem: T, predicate: (T)->Boolean): T? {
    val idx = indexOfFirst(predicate)
    return if (idx>=0) { set(idx, newItem); newItem } else null
}
