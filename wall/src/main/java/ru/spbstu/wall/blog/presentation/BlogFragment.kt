package ru.spbstu.wall.blog.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.wall.R
import ru.spbstu.wall.databinding.FragmentBlogBinding
import ru.spbstu.wall.di.WallApi
import ru.spbstu.wall.di.WallComponent
import javax.inject.Inject

class BlogFragment : Fragment() {

    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: BlogViewModel

    private val adapter: BlogAdapter = BlogAdapter(onUserAvatarClick = {
        viewModel.onUserAvatarClick()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentBlogBinding.inflate(inflater, container, false)
        binding.frgBlogRvPosts.adapter = adapter
        binding.frgBlogToolbarTitle.text = "Главная"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                adapter.bingData(it.blogs)
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