package com.example.catconnect.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.catconnect.R
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.data.session.SessionManager
import com.example.catconnect.databinding.FragmentCreateAccountBinding
import com.google.android.material.snackbar.Snackbar

class CreateAccountFragment : Fragment() {

    private var _binding: FragmentCreateAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateAccountBinding.inflate(inflater, container, false)
        session = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateAccount.setOnClickListener {
            handleCreateAccount()
        }
    }

    private fun handleCreateAccount() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        // --- Validasi Input ---
        var isValid = true
        if (name.isBlank()) {
            binding.tilName.error = "Nama wajib diisi"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        if (email.isBlank() || !email.contains("@")) {
            binding.tilEmail.error = "Email tidak valid"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (password.length < 6) {
            binding.tilPassword.error = "Password minimal 6 karakter"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        if (password != confirmPassword) {
            binding.tilConfirmPassword.error = "Password tidak cocok"
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        if (!isValid) return

        // --- Proses Pembuatan Akun ---
        val newUser = FakeRepository.createUser(name, email, password)

        if (newUser != null) {
            // Sukses: simpan session dan navigasi
            session.login(newUser.id, newUser.email)
            Snackbar.make(requireView(), "Akun berhasil dibuat! Selamat datang, ${newUser.name}", Snackbar.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_global_to_feedFragment)
        } else {
            // Gagal: kemungkinan email sudah terdaftar
            binding.tilEmail.error = "Email ini sudah terdaftar"
            Snackbar.make(requireView(), "Gagal membuat akun. Email mungkin sudah digunakan.", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
