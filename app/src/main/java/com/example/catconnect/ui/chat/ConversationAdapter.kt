package com.example.catconnect.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.catconnect.databinding.ItemConversationBinding

class ConversationAdapter(
    private val conversations: List<Conversation>,
    private val onItemClicked: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val binding = ItemConversationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ConversationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(conversations[position])
    }

    override fun getItemCount(): Int = conversations.size

    inner class ConversationViewHolder(private val binding: ItemConversationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(conversation: Conversation) {
            binding.ivProfile.load(conversation. userImageUrl) {
                crossfade(true)
            }
            binding.tvName.text = conversation.userName
            binding.tvLastMessage.text = conversation.lastMessage

            itemView.setOnClickListener {
                onItemClicked(conversation)
            }
        }
    }
}
