package ru.spbstu.search.search.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import ru.spbstu.search.R
import ru.spbstu.search.databinding.FragmentSearchBinding
import ru.spbstu.search.di.SearchApi
import ru.spbstu.search.di.SearchComponent
import javax.inject.Inject

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: SearchViewModel

    private val adapter: SearchAdapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inject()
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.frgSearchToolbarTitle.text = "Поиск"
        binding.frgSearchRvSearchResults.adapter = adapter
        binding.frgSearchRvSearchResults.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(requireContext(), R.drawable.item_decoration_drawable)!!
                )
            }
        )
        binding.frgSearchEtSearch.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.onNewText(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.state.filterNotNull().collect {
                adapter.bingData(it.searchs)
            }
        }
    }

    private fun inject() {
        FeatureUtils.getFeature<SearchComponent>(this, SearchApi::class.java)
            .searchSearchComponentFactory()
            .create(this)
            .inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }
}