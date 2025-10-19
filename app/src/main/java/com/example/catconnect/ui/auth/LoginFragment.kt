package com.example.catconnect.ui.auth

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.catconnect.MainActivity
import com.example.catconnect.R
import com.example.catconnect.data.repo.FakeRepository
import com.example.catconnect.data.session.SessionManager
import com.example.catconnect.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var session: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        session = SessionManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the logo from URL
        binding.ivLogo.load("https://www.pngfind.com/pngs/m/45-456947_cat-5-icons-png-cat-silhouette-clip-art.png")

        // jika sudah login, langsung ke feed
        if (session.isLoggedIn()) {
            findNavController().navigate(R.id.action_global_to_feedFragment)
            return
        }

        // Membuat teks "Sign Up" menjadi tebal
        val text = "Don't have an account? <b>Sign Up</b>"
        binding.tvCreateAccount.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }

        binding.btnLogin.setOnClickListener {
            // 1. Ambil input dari UI
            val email = binding.etEmail.text?.toString()?.trim().orEmpty()
            val password = binding.etPassword.text?.toString().orEmpty()

            // 2. Validasi input
            var isValid = true
            if (email.isBlank() || !email.contains("@")) {
                binding.tilEmail.error = "Email tidak valid"
                isValid = false
            } else {
                binding.tilEmail.error = null
            }

            if (password.isBlank()) {
                binding.tilPassword.error = "Password wajib diisi"
                isValid = false
            } else {
                binding.tilPassword.error = null
            }

            if (!isValid) return@setOnClickListener

            // 3. Panggil repository untuk login
            val user = FakeRepository.login(email, password)

            // 4. Proses hasil login
            if (user != null) {
                // Login sukses: simpan session dan navigasi ke feed
                session.login(user.id, user.email)
                Snackbar.make(view, "Login sukses! Selamat datang, ${user.name}", Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_to_feedFragment)
            } else {
                // Login gagal: tampilkan pesan error
                Snackbar.make(view, "Email atau password salah", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Sembunyikan AppBar saat fragmen ini ditampilkan
        (activity as? MainActivity)?.showAppBar(false)
    }

    override fun onPause() {
        super.onPause()
        // Tampilkan kembali AppBar saat fragmen ini ditinggalkan
        (activity as? MainActivity)?.showAppBar(true)
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
