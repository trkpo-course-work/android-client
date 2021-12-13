package ru.spbstu.wall.blog.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.wall.WallRouter
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.wall.repository.WallRepository
import timber.log.Timber

class BlogViewModel(
    private val wallRouter: WallRouter,
    private val wallRepository: WallRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val disposable = CompositeDisposable()

    init {
        loadData()
    }

    fun onUserAvatarClick(blog: Blog) {
        wallRouter.openUserProfile(blog.user.id)
    }

    fun loadData() {
        wallRepository.getNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось загрузить новости"
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

    data class State(val blogs: List<Blog>)

    companion object {
        private val TAG = BlogViewModel::class.simpleName
    }
}