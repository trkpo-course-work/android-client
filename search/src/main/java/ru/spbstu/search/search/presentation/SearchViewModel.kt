package ru.spbstu.search.search.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.search.SearchRouter
import ru.spbstu.search.repository.SearchRepository
import ru.spbstu.search.search.domain.SearchResult
import timber.log.Timber

class SearchViewModel(
    private val router: SearchRouter,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private var data: MutableList<SearchResult> = mutableListOf()

    private val disposable = CompositeDisposable()

    private var lastText = ""

    fun onNewText(text: CharSequence) {
        lastText = text.toString()
        if (text.isBlank()) {
            _state.value =
                State(emptyList())
        } else {
            _state.value =
                State(data.filter { it.name.contains(text, true) || it.login.contains(text, true) })
        }
    }

    init {
        loadData()
    }

    fun onUserClick(id: Long) {
        router.goToUserProfile(id)
    }

    fun changeFavState(id: Long, newFav: Boolean) {
        searchRepository.setFavorite(id, newFav)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        loadData()
                    }
                    is BlogInResult.Error -> {

                    }
                }
            }, {
                Timber.e(TAG, it)
            })
            .addTo(disposable)
    }

    private fun loadData() {
        searchRepository.getSearchResults()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        data.clear()
                        data.addAll(it.data)
                        onNewText(lastText)
                    }
                    is BlogInResult.Error -> {

                    }
                }
            }, {
                Timber.e(TAG, it)
            })
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    data class State(val searchs: List<SearchResult>)

    companion object {
        private val TAG = SearchViewModel::class.simpleName
    }
}