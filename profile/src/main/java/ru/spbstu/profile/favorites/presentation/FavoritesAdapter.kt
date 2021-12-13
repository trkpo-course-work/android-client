package ru.spbstu.profile.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.spbstu.profile.databinding.ItemFavoriteBinding
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.common.extensions.setDebounceClickListener

class FavoritesAdapter(
    private val onItemClick: (UserProfile) -> Unit,
    private val onDeleteClick: (UserProfile) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {
    private val searchs: ArrayList<UserProfile> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(
            ItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun bingData(newSearchs: List<UserProfile>) {
        val callback = SearchDiffUtilCallback(searchs, newSearchs)
        val diffResult = DiffUtil.calculateDiff(callback)
        searchs.clear()
        searchs.addAll(newSearchs)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(searchs[position])
    }

    override fun getItemCount(): Int = searchs.size

    inner class FavoritesViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: UserProfile) {
            binding.itemFavoriteTvName.text = favorite.name
            binding.root.setDebounceClickListener {
                onItemClick.invoke(favorite)
            }

            binding.itemFavoriteIbRemove.setDebounceClickListener {
                onDeleteClick.invoke(favorite)
            }
        }
    }

    class SearchDiffUtilCallback(
        private val oldList: List<UserProfile>,
        private val newList: List<UserProfile>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}