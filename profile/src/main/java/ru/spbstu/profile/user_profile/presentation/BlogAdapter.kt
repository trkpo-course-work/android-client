package ru.spbstu.profile.user_profile.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.common.utils.getSpannableText
import ru.spbstu.profile.databinding.ItemProfilePostBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BlogAdapter(private val pictureUrlHelper: PictureUrlHelper) :
    RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    private val blogs: ArrayList<Blog> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(
            ItemProfilePostBinding.inflate(
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

    inner class BlogViewHolder(private val binding: ItemProfilePostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fun bind(blog: Blog) {
            val pictureId = blog.pictureId
            if (pictureId != null) {
                binding.itemPostIvImage.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(pictureUrlHelper.getPictureUrl(pictureId))
                    .centerCrop()
                    .into(binding.itemPostIvImage)
            } else {
                binding.itemPostIvImage.visibility = View.GONE
            }

            val avatarId = blog.user.pictureId
            if (avatarId != null) {
                Glide.with(binding.root)
                    .load(pictureUrlHelper.getPictureUrl(avatarId))
                    .centerCrop()
                    .into(binding.itemPostIvAvatar)
            }

            binding.itemPostTvDate.text = dateFormat.format(Date(blog.dateTime))
            binding.itemPostTvName.text = blog.user.name
            binding.itemPostTvPost.text = getSpannableText(blog.spans, blog.text)

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