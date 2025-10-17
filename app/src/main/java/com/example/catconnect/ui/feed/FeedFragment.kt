package com.example.catconnect.ui.feed

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.catconnect.R
import com.example.catconnect.data.model.Post
import com.example.catconnect.ui.mappers.toUi
import com.google.android.material.snackbar.Snackbar

class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val vm: FeedViewModel by viewModels()
    private lateinit var adapter: PostAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rv)

        adapter = PostAdapter(
            onLike = { postUi ->
                // Panggil ViewModel untuk memperbarui data di latar belakang
                vm.toggleLike(postUi.id)

                // Perbarui UI secara instan
                val currentList = adapter.currentList.toMutableList()
                val index = currentList.indexOfFirst { it.id == postUi.id }
                if (index != -1) {
                    val newLikedState = !postUi.isLiked
                    val newLikeCount = if (newLikedState) postUi.likes + 1 else postUi.likes - 1
                    val updatedPost = postUi.copy(
                        isLiked = newLikedState,
                        likes = newLikeCount
                    )
                    currentList[index] = updatedPost
                    adapter.submitList(currentList)
                }
            },
            onComment = {
                val b = Bundle().apply { putString("postId", it.id) }
                findNavController().navigate(R.id.postDetailFragment, b)
            },
            onShare = {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Check out this cat on CatConnect!\n${it.photoUrl}")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            },
            onSave = { postUi ->
                vm.toggleSave(postUi.id)

                // Perbarui UI secara instan
                val currentList = adapter.currentList.toMutableList()
                val index = currentList.indexOfFirst { it.id == postUi.id }
                if (index != -1) {
                    val updatedPost = postUi.copy(isSaved = !postUi.isSaved)
                    currentList[index] = updatedPost
                    adapter.submitList(currentList)
                }
                val msg = if (!postUi.isSaved) "Post saved" else "Post unsaved"
                Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).show()
            },
            onClick = { postUi ->
                val b = Bundle().apply { putString("postId", postUi.id) }
                findNavController().navigate(R.id.postDetailFragment, b)
            }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        vm.filteredPosts.observe(viewLifecycleOwner) { list: List<Post> ->
            adapter.submitList(list.map { it.toUi() })
            view.findViewById<View>(R.id.emptyState).visibility =
                if (list.isEmpty()) View.VISIBLE else View.GONE
        }
    }
}
