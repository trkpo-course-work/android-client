package ru.spbstu.wall.blog.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.wall.databinding.ItemPostBinding

class BlogAdapter(private val onUserAvatarClick: () -> Unit) :
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
        blogs.clear()
        blogs.addAll(newBlogs)
        val diffResult = DiffUtil.calculateDiff(callback)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(blogs[position])
    }

    override fun getItemCount(): Int = blogs.size

    inner class BlogViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blog: Blog) {
            Glide.with(binding.root)
                .load(blog.avatarUrl)
                .centerCrop()
                .into(binding.itemPostIvAvatar)

            binding.itemPostTvDate.text = blog.date
            binding.itemPostTvName.text = blog.name
            binding.itemPostTvPost.text = blog.post

            if (blog.photoUrl != null) {
                binding.itemPostIvImage.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(blog.photoUrl)
                    .centerCrop()
                    .into(binding.itemPostIvImage)
            } else {
                binding.itemPostIvImage.visibility = View.GONE
            }

            binding.itemPostIvAvatar.setDebounceClickListener {
                onUserAvatarClick.invoke()
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