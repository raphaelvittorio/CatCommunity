package com.example.catconnect.ui.post

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.catconnect.R
import com.example.catconnect.data.model.Post
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.databinding.FragmentAddPostBinding
import com.google.android.material.snackbar.Snackbar

class AddPostFragment : Fragment() {

    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    private var pickedUri: Uri? = null          // foto yang dipilih
    private var editingPostId: String? = null   // id post kalau mode edit
    private var editingPost: Post? = null       // data post lama

    // launcher pilih gambar dari galeri
    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        pickedUri = uri
        if (uri != null) {
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

        // --- PERBAIKAN: Hapus referensi ke 'title' ---
        // Cek apakah ini mode edit
        editingPostId = arguments?.getString("postId")
        editingPostId?.let { id ->
            editingPost = FakeRepository.getPost(id)
            editingPost?.let { p ->
                // Hapus p.title, karena sudah tidak ada
                binding.etBreed.setText(p.breed)
                binding.etAge.setText(p.ageMonth.toString())
                binding.etCaption.setText(p.caption)
                binding.imgPreview.load(p.photoUrl)
                binding.btnSave.text = getString(R.string.update_post)
            }
        }

        // Hapus TextInputLayout untuk title dari XML jika masih ada
        // binding.tilTitle.visibility = View.GONE

        // Pilih foto
        binding.btnPickImage.setOnClickListener { pickImage.launch("image/*") }

        // Simpan
        binding.btnSave.setOnClickListener {
            if (!validate()) return@setOnClickListener

            // --- PERBAIKAN: Hapus pengambilan 'title' ---
            val breed = binding.etBreed.text.toString().trim()
            val age = binding.etAge.text.toString().toInt()
            val caption = binding.etCaption.text.toString().trim()

            // Tentukan URL foto
            val photoUrl = pickedUri?.toString()
                ?: editingPost?.photoUrl
                ?: "https://picsum.photos/seed/${System.currentTimeMillis()}/800/500"

            if (editingPost == null) {
                // --- PERBAIKAN: Buat Post baru tanpa 'title' ---
                val newPost = Post(
                    id = "p${System.currentTimeMillis()}",
                    userId = FakeRepository.currentUser.id,
                    photoUrl = photoUrl,
                    caption = caption,
                    breed = breed,
                    ageMonth = age,
                    likes = 0,
                    isLiked = false, // Nilai default
                    isSaved = false  // Nilai default
                )
                FakeRepository.addPost(newPost)
                Snackbar.make(view, "Post ditambahkan", Snackbar.LENGTH_SHORT).show()
            } else {
                // --- PERBAIKAN: Update Post tanpa 'title' ---
                val updatedPost = editingPost!!.copy(
                    breed = breed,
                    ageMonth = age,
                    caption = caption,
                    photoUrl = photoUrl
                )
                FakeRepository.updatePost(updatedPost)
                Snackbar.make(view, "Post diperbarui", Snackbar.LENGTH_SHORT).show()
            }
            findNavController().navigateUp()
        }
    }

    private fun validate(): Boolean {
        var ok = true
        with(binding) {
            // --- PERBAIKAN: Hapus validasi untuk 'title' ---
            // tilTitle.error = if (etTitle.text.isNullOrBlank()) { ok = false; "Required" } else null
            tilBreed.error = if (etBreed.text.isNullOrBlank()) { ok = false; "Required" } else null
            tilCaption.error = if (etCaption.text.isNullOrBlank()) { ok = false; "Required" } else null
            val a = etAge.text?.toString()?.toIntOrNull()
            tilAge.error = if (a == null || a < 0) { ok = false; "Age ≥ 0" } else null
        }
        // Tampilkan Snackbar jika ada foto yang belum dipilih saat membuat post baru
        if (editingPost == null && pickedUri == null) {
            Snackbar.make(requireView(), "Please pick an image", Snackbar.LENGTH_SHORT).show()
            ok = false
        }
        return ok
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
