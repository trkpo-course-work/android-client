package ru.spbstu.diary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.common.utils.getSpannableText
import ru.spbstu.diary.databinding.ItemNoteBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DiaryAdapter(
    private val urlHelper: PictureUrlHelper,
    private val onActionsClick: (View, Blog) -> Unit
) : RecyclerView.Adapter<DiaryAdapter.NoteViewHolder>() {
    private val notes: ArrayList<Blog> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun bingData(newBlogs: List<Blog>) {
        val callback = BlogDiffUtilCallback(notes, newBlogs)
        val diffResult = DiffUtil.calculateDiff(callback)
        notes.clear()
        notes.addAll(newBlogs)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount(): Int = notes.size

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blog: Blog) {
            binding.itemNoteTvDate.text = dateFormat.format(blog.dateTime)
            binding.itemNoteTvNote.text = getSpannableText(blog.spans, blog.text)

            val pictureId = blog.pictureId
            if (pictureId != null) {
                binding.itemNoteIvImage.visibility = View.VISIBLE
                Glide.with(binding.root)
                    .load(urlHelper.getPictureUrl(pictureId))
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_drawable)
                    .into(binding.itemNoteIvImage)
            } else {
                binding.itemNoteIvImage.visibility = View.GONE
            }
            binding.itemNoteIvActions.setOnClickListener {
                onActionsClick.invoke(it, blog)
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