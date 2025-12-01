package com.example.catconnect.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.catconnect.R
import com.example.catconnect.databinding.ItemCommentBinding

class CommentAdapter(private val onItemClicked: ((CommentUi) -> Unit)? = null) :
    ListAdapter<CommentUi, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
        onItemClicked?.let { listener ->
            holder.itemView.setOnClickListener { listener(comment) }
        }
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: CommentUi) {
            binding.imgProfile.load(comment.authorPhotoUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
                placeholder(R.drawable.baseline_account_circle_24)
                error(R.drawable.baseline_account_circle_24)
            }

            binding.tvUsername.visibility = View.GONE

            val formattedText = "<b>${comment.authorName}</b> ${comment.comment}"
            binding.tvComment.text = HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<CommentUi>() {
    override fun areItemsTheSame(oldItem: CommentUi, newItem: CommentUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CommentUi, newItem: CommentUi): Boolean {
        return oldItem == newItem
    }
}