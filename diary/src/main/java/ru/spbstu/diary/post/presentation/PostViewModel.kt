package ru.spbstu.diary.post.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.diary.DiaryRouter

class PostViewModel(private val router: DiaryRouter) : ViewModel() {
    var photoUri: Uri? = null
        set(value) {
            field = value
            _photoState.value = value
        }

    private val _photoState = MutableStateFlow<Uri?>(null)
    val photoState = _photoState.asStateFlow()

    fun back() {
        router.pop()
    }
}