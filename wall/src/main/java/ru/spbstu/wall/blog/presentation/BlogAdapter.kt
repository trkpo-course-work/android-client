package ru.spbstu.wall.blog.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.common.utils.getSpannableText
import ru.spbstu.wall.R
import ru.spbstu.wall.databinding.ItemPostBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BlogAdapter(
    private val pictureUrlHelper: PictureUrlHelper,
    private val onUserAvatarClick: (Blog) -> Unit
) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    private val blogs: ArrayList<Blog> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(
            ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun bingData(newBlogs: List<Blog>) {
        val callback = BlogDiffUtilCallback(blogs, newBlogs)
        val diffResult = DiffUtil.calculateDiff(callback)
        blogs.clear()
        blogs.addAll(newBlogs)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(blogs[position])
    }

    override fun getItemCount(): Int = blogs.size

    inner class BlogViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        fun bind(blog: Blog) {

            val avatarId = blog.user.pictureId
            if (avatarId != null) {
                Glide.with(binding.root)
                    .load(pictureUrlHelper.getPictureUrl(avatarId))
                    .centerCrop()
                    .into(binding.itemPostIvAvatar)
            }

            binding.itemPostTvDate.text = dateFormat.format(blog.dateTime)
            binding.itemPostTvName.text = blog.user.name
            binding.itemPostTvPost.text = getSpannableText(blog.spans, blog.text)

            val pictureId = blog.pictureId
            if (pictureId != null) {
                binding.itemPostIvImage.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(pictureUrlHelper.getPictureUrl(pictureId))
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_drawable)
                    .into(binding.itemPostIvImage)
            } else {
                binding.itemPostIvImage.visibility = View.GONE
            }

            binding.itemPostIvAvatar.setDebounceClickListener {
                onUserAvatarClick.invoke(blog)
            }
        }
    }

    class BlogDiffUtilCallback(private val oldList: List<Blog>, private val newList: List<Blog>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}