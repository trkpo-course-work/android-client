package ru.spbstu.diary.user_diary.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.common.extensions.setDebounceClickListener
import ru.spbstu.diary.DiaryAdapter
import ru.spbstu.diary.databinding.FragmentUserDiaryBinding
import ru.spbstu.diary.databinding.LayoutUserNotesBinding
import ru.spbstu.diary.di.DiaryApi
import ru.spbstu.diary.di.DiaryComponent
import javax.inject.Inject

class UserDiaryFragment : Fragment() {

    private var _binding: FragmentUserDiaryBinding? = null
    private val binding get() = _binding!!

    private var _postsBinding: LayoutUserNotesBinding? = null
    private val postsBinding get() = _postsBinding!!

    private val adapter = DiaryAdapter()

    @Inject
    lateinit var viewModel: UserDiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentUserDiaryBinding.inflate(inflater, container, false)
        _postsBinding = LayoutUserNotesBinding.bind(binding.root)
        postsBinding.layoutNotesRvPosts.adapter = adapter
        postsBinding.layoutNotesRvPosts.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((postsBinding.layoutNotesRvPosts.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition() == 0) {
                    binding.frgUserDiaryFab.visibility = View.VISIBLE
                } else {
                    binding.frgUserDiaryFab.visibility = View.GONE
                }
            }
        })
        binding.frgUserDiaryFab.setDebounceClickListener {
            viewModel.openPostFragment(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                if (it.blogs.isEmpty()) {
                    binding.frgUserDiaryFab.visibility = View.VISIBLE
                }
                adapter.bingData(it.blogs)
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<DiaryComponent>(this, DiaryApi::class.java)
            .userDiaryComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserDiaryFragment()
    }
}