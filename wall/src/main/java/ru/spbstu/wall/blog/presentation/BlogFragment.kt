package ru.spbstu.wall.blog.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.wall.databinding.FragmentBlogBinding
import ru.spbstu.wall.di.WallApi
import ru.spbstu.wall.di.WallComponent

class BlogFragment : Fragment() {

    private var _binding: FragmentBlogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentBlogBinding.inflate(inflater, container, false)
        return binding.root
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