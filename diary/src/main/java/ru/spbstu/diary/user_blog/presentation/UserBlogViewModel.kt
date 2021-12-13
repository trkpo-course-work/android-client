package ru.spbstu.diary.user_blog.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.repository.DiaryRepository
import timber.log.Timber

class UserBlogViewModel(
    private val router: DiaryRouter,
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        diaryRepository.getPublicBlogs()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось получить ваши посты"
                    }
                }
            }, {
                Timber.e(TAG, it)
            })
    }

    fun deleteBlog(blog: Blog) {
        diaryRepository.deleteBlog(blog.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        loadData()
                        _error.value = "Блог успешно удалён"
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось получить ваши посты"
                    }
                }
            }, {
                Timber.e(TAG, it)
            })
    }

    fun editBlog(blog: Blog) {
        router.navigateToPostFragment(true, true, blog)
    }

    fun openPostFragment(isEdit: Boolean) {
        router.navigateToPostFragment(true, isEdit, null)
    }

    data class State(val blogs: List<Blog>)

    companion object {
        private val TAG = UserBlogViewModel::class.simpleName
    }
}