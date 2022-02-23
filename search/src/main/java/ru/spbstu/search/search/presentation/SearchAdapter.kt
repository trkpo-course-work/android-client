package ru.spbstu.search.search.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.search.R
import ru.spbstu.search.databinding.ItemSearchBinding
import ru.spbstu.search.search.domain.SearchResult

class SearchAdapter(
    private val pictureUrlHelper: PictureUrlHelper,
    private val onUserClick: (Long) -> Unit,
    private val onFavoriteClick: (SearchResult) -> Unit
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    private val searchs: ArrayList<SearchResult> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun bingData(newSearchs: List<SearchResult>) {
        val callback = SearchDiffUtilCallback(searchs, newSearchs)
        val diffResult = DiffUtil.calculateDiff(callback)
        searchs.clear()
        searchs.addAll(newSearchs)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(searchs[position])
    }

    override fun getItemCount(): Int = searchs.size

    inner class SearchViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(search: SearchResult) {
            binding.itemSearchResult.text = search.name
            binding.root.setDebounceClickListener {
                onUserClick.invoke(search.userId)
            }
            binding.itemSearchFavorite.isChecked = search.isFav
            binding.itemSearchFavorite.setDebounceClickListener {
                onFavoriteClick.invoke(search)
            }
            val pictureId = search.pictureId
            if (pictureId != null) {
                Glide.with(binding.root)
                    .load(pictureUrlHelper.getPictureUrl(pictureId))
                    .centerCrop()
                    .into(binding.itemSearchAvartar)
            } else {
                Glide.with(binding.root)
                    .load(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_unknown_user_30
                        )
                    )
                    .centerCrop()
                    .into(binding.itemSearchAvartar)
            }
        }
    }

    class SearchDiffUtilCallback(
        private val oldList: List<SearchResult>,
        private val newList: List<SearchResult>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].userId == newList[newItemPosition].userId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}