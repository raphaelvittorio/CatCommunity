package com.example.catconnect.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.catconnect.data.repo.FakeDb
import com.example.catconnect.databinding.FragmentEditProfileBinding
import com.google.android.material.snackbar.Snackbar

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Muat data pengguna saat ini
        loadUserData()

        // Atur tombol kembali pada toolbar
        binding.editProfileToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        // Atur tombol simpan
        binding.btnSaveProfile.setOnClickListener {
            saveUserData()
        }
    }

    private fun loadUserData() {
        val currentUser = FakeDb.currentUser
        binding.etProfileName.setText(currentUser.name)
        binding.etProfileBio.setText(currentUser.bio)
    }

    private fun saveUserData() {
        val newName = binding.etProfileName.text.toString().trim()
        val newBio = binding.etProfileBio.text.toString().trim()

        if (newName.isEmpty()) {
            Snackbar.make(requireView(), "Nama tidak boleh kosong", Snackbar.LENGTH_SHORT).show()
            return
        }

        // Update data di FakeDb menggunakan .copy()
        FakeDb.currentUser = FakeDb.currentUser.copy(
            name = newName,
            bio = newBio
        )

        Snackbar.make(requireView(), "Profil berhasil diperbarui", Snackbar.LENGTH_SHORT).show()

        // Kembali ke halaman profil
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}