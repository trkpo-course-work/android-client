package ru.spbstu.wall.blog.presentation

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
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.wall.databinding.FragmentBlogBinding
import ru.spbstu.wall.di.WallApi
import ru.spbstu.wall.di.WallComponent
import javax.inject.Inject

class BlogFragment : Fragment() {

    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: BlogViewModel

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper

    private lateinit var adapter: BlogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        adapter = BlogAdapter(pictureUrlHelper) {
            viewModel.onUserAvatarClick(it)
        }
        _binding = FragmentBlogBinding.inflate(inflater, container, false)
        binding.frgBlogRvPosts.adapter = adapter
        binding.frgBlogToolbarTitle.text = "Главная"
        binding.frgBlogRefresh.setDebounceClickListener {
            viewModel.loadData()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                adapter.bingData(it.blogs)
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<WallComponent>(this, WallApi::class.java)
            .blogComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = BlogFragment()
    }
}