package com.example.catconnect.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.catconnect.R
import com.example.catconnect.databinding.ItemPostBinding

data class PostUi(
    val id: String,
    val photoUrl: String?,
    val authorName: String?,
    val breed: String?,
    val ageMonth: Int?,
    val caption: String?,
    val likes: Int,
    val isLiked: Boolean = false,
    val isSaved: Boolean = false, // Added for save state
    val authorPhotoUrl: String? = null
)

class PostAdapter(
    private val onLike: (PostUi) -> Unit,
    private val onComment: (PostUi) -> Unit,
    private val onShare: (PostUi) -> Unit,
    private val onSave: (PostUi) -> Unit, // Added for save action
    private val onMore: ((PostUi) -> Unit)? = null,
    private val onClick: ((PostUi) -> Unit)? = null
) : ListAdapter<PostUi, PostAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<PostUi>() {
        override fun areItemsTheSame(oldItem: PostUi, newItem: PostUi) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: PostUi, newItem: PostUi) = oldItem == newItem
    }

    inner class VH(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, /* attachToParent = */ false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val b = holder.binding

        // Header
        b.tvUsername.text = item.authorName.orEmpty().ifBlank { "Anonymous" }
        b.imgProfile.load(item.authorPhotoUrl) {
            crossfade(true)
            placeholder(R.drawable.baseline_account_circle_24)
            error(R.drawable.baseline_account_circle_24)
            transformations(coil.transform.CircleCropTransformation())
        }

        // Post Image
        b.imgPost.load(item.photoUrl) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
        }

        // Action Buttons State
        val likeIcon = if (item.isLiked) R.drawable.ic_like_filled else R.drawable.ic_like_outline
        b.btnLike.setImageResource(likeIcon)

        val saveIcon = if (item.isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline
        b.btnBookmark.setImageResource(saveIcon)

        // Action Buttons Click Listeners
        b.btnLike.setOnClickListener { onLike(item) }
        b.btnComment.setOnClickListener { onComment(item) }
        b.btnShare.setOnClickListener { onShare(item) }
        b.btnBookmark.setOnClickListener { onSave(item) }
        b.btnMore.setOnClickListener { onMore?.invoke(item) }

        // Likes Count
        b.tvLikes.text = holder.itemView.context.resources.getQuantityString(R.plurals.like_count, item.likes, item.likes)

        // Caption
        val captionText = "<b>${item.authorName.orEmpty().ifBlank { "Anonymous" }}</b> ${item.caption.orEmpty()}"
        b.tvCaption.text = android.text.Html.fromHtml(captionText, android.text.Html.FROM_HTML_MODE_COMPACT)

        // Click on image to like
        b.imgPost.setOnClickListener {
            // You can add a double-tap listener here for a better UX
            onLike(item)
        }
        
        // Optional: Click on root goes to detail
        b.root.setOnClickListener { onClick?.invoke(item) }
    }
}
