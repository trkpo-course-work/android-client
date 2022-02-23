package ru.spbstu.diary.user_blog.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.commonmark.parser.Parser
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.common.utils.PictureUrlHelper
import ru.spbstu.diary.DiaryAdapter
import ru.spbstu.diary.R
import ru.spbstu.diary.databinding.FragmentUserBlogBinding
import ru.spbstu.diary.databinding.LayoutUserNotesBinding
import ru.spbstu.diary.di.DiaryApi
import ru.spbstu.diary.di.DiaryComponent
import javax.inject.Inject

class UserBlogFragment : Fragment() {

    private var _binding: FragmentUserBlogBinding? = null
    private val binding get() = _binding!!

    private var _postsBinding: LayoutUserNotesBinding? = null
    private val postsBinding get() = _postsBinding!!

    @Inject
    lateinit var pictureUrlHelper: PictureUrlHelper

    @Inject
    lateinit var parser: Parser

    private lateinit var adapter: DiaryAdapter

    @Inject
    lateinit var viewModel: UserBlogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentUserBlogBinding.inflate(inflater, container, false)
        _postsBinding = LayoutUserNotesBinding.bind(binding.root)
        adapter = DiaryAdapter(pictureUrlHelper, parser) { v, blog ->
            val menu = PopupMenu(requireContext(), v)
            menu.menuInflater.inflate(R.menu.menu_post_actions, menu.menu)
            menu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> {
                        viewModel.editBlog(blog)
                        true
                    }
                    R.id.delete -> {
                        viewModel.deleteBlog(blog)
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }
        postsBinding.layoutNotesRvPosts.adapter = adapter
        postsBinding.layoutNotesRvPosts.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((postsBinding.layoutNotesRvPosts.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0 || adapter.itemCount == 0) {
                    binding.frgUserBlogFab.visibility = View.VISIBLE
                } else {
                    binding.frgUserBlogFab.visibility = View.GONE
                }
            }
        })
        binding.frgUserBlogFab.setDebounceClickListener {
            viewModel.openPostFragment(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                if (it.blogs.isEmpty()) {
                    binding.frgUserBlogFab.visibility = View.VISIBLE
                }
                adapter.bingData(it.blogs)
            }
        }

        lifecycleScope.launch {
            viewModel.error.filterNotNull().collect {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadData()
    }

    private fun inject() {
        FeatureUtils.getFeature<DiaryComponent>(this, DiaryApi::class.java)
            .userBlogComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserBlogFragment()
    }
}