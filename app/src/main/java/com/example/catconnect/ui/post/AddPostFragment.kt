package com.example.catconnect.ui.post

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.catconnect.data.model.Post
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.databinding.FragmentAddPostBinding

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private var pickedUri: Uri? = null
    private var editingPost: Post? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            pickedUri = uri
            binding.imgPreview.load(uri) { crossfade(true) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DIHAPUS: Panggilan setSupportActionBar yang menyebabkan konflik
        // (activity as? AppCompatActivity)?.setSupportActionBar(binding.addPostToolbar)

        setupMode()
        setupListeners()
    }

    private fun setupMode() {
        val editingPostId = arguments?.getString("postId")
        if (editingPostId == null) {
            binding.addPostToolbar.title = "Create Post"
            binding.btnSave.text = "Save Post"
        } else {
            binding.addPostToolbar.title = "Edit Post"
            binding.btnSave.text = "Update Post"
            editingPost = FakeRepository.getPost(editingPostId)
            editingPost?.let { post ->
                binding.etCaption.setText(post.caption)
                binding.imgPreview.load(post.photoUrl)
            }
        }
    }

    private fun setupListeners() {
        binding.addPostToolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        binding.btnPickImage.setOnClickListener { pickImage.launch("image/*") }
        binding.btnSave.setOnClickListener { savePost() }
    }

    private fun savePost() {
        if (!validateInputs()) return

        val caption = binding.etCaption.text.toString().trim()

        if (editingPost == null) {
            val photoUrl = pickedUri?.toString() ?: "https://cataas.com/cat/says/Hello?width=500&height=500&time=${System.currentTimeMillis()}"

            val newPost = Post(
                id = "p${System.currentTimeMillis()}",
                userId = FakeRepository.currentUser.id,
                photoUrl = photoUrl,
                caption = caption,
                breed = "",
                ageMonth = 0,
                likes = 0,
                isLiked = false,
                isSaved = false
            )
            FakeRepository.addPost(newPost)
            Toast.makeText(requireContext(), "Post created successfully", Toast.LENGTH_SHORT).show()
        } else {
            val photoUrl = pickedUri?.toString() ?: editingPost!!.photoUrl

            val updatedPost = editingPost!!.copy(
                caption = caption,
                photoUrl = photoUrl
            )
            FakeRepository.updatePost(updatedPost)
            Toast.makeText(requireContext(), "Post updated successfully", Toast.LENGTH_SHORT).show()
        }

        findNavController().popBackStack()
    }

    private fun validateInputs(): Boolean {
        binding.tilCaption.error = null

        if (binding.etCaption.text.isNullOrBlank()) {
            binding.tilCaption.error = "Caption is required"
            return false
        }

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // DIHAPUS: Panggilan setSupportActionBar(null) yang menyebabkan konflik
        _binding = null
    }
}
