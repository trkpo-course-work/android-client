package ru.spbstu.profile.favorites.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.profile.databinding.FragmentFavoritesBinding
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileComponent
import javax.inject.Inject

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: FavoritesViewModel

    private val adapter: FavoritesAdapter = FavoritesAdapter({
        viewModel.openUserProfile(it)
    }, {
        viewModel.deleteFromFavorite(it)
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        binding.frgFavoritesRvFavorites.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                adapter.bingData(it.favorites)
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadData()
    }

    private fun inject() {
        FeatureUtils.getFeature<ProfileComponent>(this, ProfileApi::class.java)
            .favoritesComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }
}