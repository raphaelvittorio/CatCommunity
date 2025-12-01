package com.example.catconnect.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.catconnect.databinding.ItemPostGridBinding
import com.example.catconnect.ui.feed.PostUi

class PostGridAdapter(private val onClick: (PostUi) -> Unit) :
    ListAdapter<PostUi, PostGridAdapter.PostGridViewHolder>(PostUiDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostGridViewHolder {
        val binding = ItemPostGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostGridViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
        holder.itemView.setOnClickListener { onClick(post) }
    }

    inner class PostGridViewHolder(private val binding: ItemPostGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostUi) {
            binding.imgPost.load(post.photoUrl)
        }
    }

    companion object PostUiDiffCallback : DiffUtil.ItemCallback<PostUi>() {
        override fun areItemsTheSame(oldItem: PostUi, newItem: PostUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostUi, newItem: PostUi): Boolean {
            return oldItem == newItem
        }
    }
}
