package com.example.catconnect.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.catconnect.R
import com.example.catconnect.databinding.FragmentPersonalChatBinding

class PersonalChatFragment : Fragment() {

    private var _binding: FragmentPersonalChatBinding? = null
    private val binding get() = _binding!!

    // Mengambil argumen (data) yang dikirim dari fragment sebelumnya dengan aman
    private val args: PersonalChatFragmentArgs by navArgs()

    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        loadDummyMessages()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupToolbar() {
        // Atur fungsi klik untuk tombol kembali
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Ambil data dari arguments
        val userName = args.userName
        val userImage = args.userImage

        // Tampilkan nama dan muat gambar di toolbar
        binding.tvToolbarUserName.text = userName
        binding.ivToolbarProfile.load(userImage) {
            crossfade(true)
            placeholder(R.drawable.baseline_account_circle_24)
            error(R.drawable.baseline_account_circle_24)
        }
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messages)
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context).apply {
                stackFromEnd = true
            }
        }
    }

    private fun loadDummyMessages() {
        // Gunakan nama pengguna untuk pesan pembuka yang lebih personal
        messages.add(ChatMessage("Halo, ini chat dengan ${args.userName}!", 2)) // Received
        messages.add(ChatMessage("Baik! Kamu gimana?", 1))     // Sent
        messages.add(ChatMessage("Aku juga baik. Lagi sibuk apa?", 2)) // Received
        messages.add(ChatMessage("Lagi santai aja nih, hehe.", 1))      // Sent
        chatAdapter.notifyDataSetChanged()
        binding.rvChat.scrollToPosition(messages.size - 1)
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString()
        if (messageText.isNotBlank()) {
            messages.add(ChatMessage(messageText, 1)) // Sent
            chatAdapter.notifyItemInserted(messages.size - 1)
            binding.rvChat.scrollToPosition(messages.size - 1)
            binding.etMessage.text.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
