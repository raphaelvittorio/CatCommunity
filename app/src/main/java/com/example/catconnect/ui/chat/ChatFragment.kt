package com.example.catconnect.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catconnect.databinding.FragmentChatBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dummy data
        val conversations = listOf(
            Conversation("1", "Luna", "Great! See you then!", "https://cdn.pixabay.com/photo/2017/02/20/18/03/cat-2083492_1280.jpg"),
            Conversation("2", "Simba", "Okay, sounds good.", "https://cdn.pixabay.com/photo/2015/03/27/13/16/cat-694730_1280.jpg"),
            Conversation("3", "Mochi", "Can you send the picture?", "https://cdn.pixabay.com/photo/2016/01/20/13/05/cat-1151519_1280.jpg"),
            Conversation("4", "Oliver", "Hahaha, that's hilarious!", "https://cdn.pixabay.com/photo/2017/07/25/01/22/cat-2536662_1280.jpg")
        )

        val adapter = ConversationAdapter(conversations) { conversation ->
            // Membuat action navigasi dengan Safe Args, mengirimkan data yang diperlukan
            val action = ChatFragmentDirections.actionChatFragmentToPersonalChatFragment(
                userName = conversation.userName,
                userImage = conversation.userImageUrl
            )
            // Menjalankan navigasi dengan action yang sudah membawa data
            findNavController().navigate(action)
        }

        binding.rvConversations.adapter = adapter
        binding.rvConversations.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
