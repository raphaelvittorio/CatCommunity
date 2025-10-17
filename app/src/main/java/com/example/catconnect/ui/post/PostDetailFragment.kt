package com.example.catconnect.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.CircleCropTransformation
import com.example.catconnect.MainActivity
import com.example.catconnect.R
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.databinding.FragmentPostDetailBinding
import com.example.catconnect.ui.feed.CommentAdapter
import com.example.catconnect.ui.feed.CommentUi
import com.example.catconnect.ui.mappers.toUi

class PostDetailFragment : Fragment() {

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentAdapter: CommentAdapter
    private var postId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postId = arguments?.getString("postId")

        if (postId == null) {
            Toast.makeText(context, "Error: Post ID is missing.", Toast.LENGTH_LONG).show()
            parentFragmentManager.popBackStack()
            return
        }

        setupToolbar()
        populatePostUi()
        setupCommentSection()
    }

    override fun onResume() {
        super.onResume()
        if (activity is MainActivity) {
            (activity as MainActivity).showAppBar(false)
        }
    }

    override fun onPause() {
        super.onPause()
        if (activity is MainActivity) {
            (activity as MainActivity).showAppBar(true)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Post"
        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun populatePostUi() {
        val post = FakeRepository.getPost(postId!!)?.toUi() ?: return
        val postBinding = binding.postLayout // Using the included layout's binding

        postBinding.imgProfile.load(post.authorPhotoUrl) {
            crossfade(true)
            transformations(CircleCropTransformation())
        }
        postBinding.tvUsername.text = post.authorName
        postBinding.imgPost.load(post.photoUrl)
        postBinding.tvLikes.text = resources.getQuantityString(R.plurals.like_count, post.likes, post.likes)
        postBinding.tvCaption.text = post.caption

        postBinding.btnLike.setImageResource(if (post.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outline)
        postBinding.btnBookmark.setImageResource(if (post.isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline)

        // Hide unused buttons in detail view
        postBinding.btnComment.visibility = View.GONE
        postBinding.btnShare.visibility = View.GONE
        postBinding.btnMore.visibility = View.GONE
    }

    private fun setupCommentSection() {
        commentAdapter = CommentAdapter()
        binding.rvComments.adapter = commentAdapter
        binding.rvComments.layoutManager = LinearLayoutManager(context)

        observeComments()

        binding.btnSend.setOnClickListener {
            val commentText = binding.etComment.text.toString().trim()
            if (commentText.isNotBlank()) {
                FakeRepository.addComment(postId!!, commentText)
                binding.etComment.text.clear()
            }
        }

        val currentUser = FakeRepository.currentUser
        binding.imgCurrentUser.load(currentUser.photoUrl) {
            crossfade(true)
            placeholder(R.drawable.baseline_account_circle_24)
            error(R.drawable.baseline_account_circle_24)
            transformations(CircleCropTransformation())
        }
    }

    private fun observeComments() {
        FakeRepository.commentsFor(postId!!).observe(viewLifecycleOwner) { comments ->
            val commentUiList = comments.mapNotNull { dataComment ->
                val author = FakeRepository.getUser(dataComment.userId)
                author?.let { user ->
                    CommentUi(
                        id = dataComment.id,
                        postId = dataComment.postId,
                        authorId = user.id,
                        authorName = user.name,
                        authorPhotoUrl = user.photoUrl,
                        comment = dataComment.text,
                        timestamp = dataComment.createdAt
                    )
                }
            }
            commentAdapter.submitList(commentUiList) {
                // Scroll to the bottom to show the new comment
                if (commentUiList.isNotEmpty()) {
                    binding.rvComments.smoothScrollToPosition(commentUiList.size - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
