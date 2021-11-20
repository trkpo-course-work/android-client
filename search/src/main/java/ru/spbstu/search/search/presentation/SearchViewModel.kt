package ru.spbstu.search.search.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.search.SearchRouter
import ru.spbstu.search.search.domain.SearchResult

class SearchViewModel(private val router: SearchRouter) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    fun onNewText(text: CharSequence) {
        if (text == "artur") {
            loadData()
        }
    }

    private fun loadData() {
        _state.value = State(
            listOf(
                SearchResult(1, "Artur"),
                SearchResult(2, "Artur4ik"),
                SearchResult(3, "Artur9n"),
            )
        )
    }

    data class State(val searchs: List<SearchResult>)
}