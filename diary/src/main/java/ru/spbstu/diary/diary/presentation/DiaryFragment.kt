package ru.spbstu.diary.diary.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import ru.spbstu.common.di.FeatureUtils
import ru.spbstu.diary.databinding.FragmentDiaryBinding
import ru.spbstu.diary.di.DiaryApi
import ru.spbstu.diary.di.DiaryComponent
import javax.inject.Inject

class DiaryFragment : Fragment() {

    private var _binding: FragmentDiaryBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: DiaryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentDiaryBinding.inflate(inflater, container, false)
        val pagerAdapter = DiaryPagerAdapter(requireActivity())
        binding.frgDiaryPager.adapter = pagerAdapter
        TabLayoutMediator(
            binding.frgDiaryTabs, binding.frgDiaryPager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Блог"
                }
                1 -> {
                    tab.text = "Дневник"
                }
            }
        }.attach()
        return binding.root
    }

    private fun inject() {
        FeatureUtils.getFeature<DiaryComponent>(this, DiaryApi::class.java)
            .diaryDiaryComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DiaryFragment()
    }
}