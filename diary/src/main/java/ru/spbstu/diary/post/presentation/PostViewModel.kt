package ru.spbstu.diary.post.presentation

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.spbstu.common.api.model.SpanType
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.Span
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.repository.DiaryRepository
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.widget.Toast
import java.io.OutputStream
import java.lang.Exception


class PostViewModel(
    private val router: DiaryRouter,
    private val diaryRepository: DiaryRepository,
    private val contentResolver: ContentResolver
) :
    ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val spansInternal = mutableListOf<Span>()
    private var photoFile: File? = null

    private val _spans = MutableStateFlow<List<Span>?>(null)
    val spans = _spans.asStateFlow()

    private val _buttonsState = MutableStateFlow<Boolean>(true)
    val buttonsState = _buttonsState.asStateFlow()

    private val disposable = CompositeDisposable()

    var userDeletedPhoto = false

    var mode: PostFragment.Mode? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    var photoUri: Uri? = null
        set(value) {
            field = value
            if (value != null) {
                contentResolver.openInputStream(value)!!.use { input ->
                    photoFile = File.createTempFile(
                        System.currentTimeMillis().toString(),
                        ".${
                            MimeTypeMap.getSingleton()
                                .getExtensionFromMimeType(contentResolver.getType(value))
                        }"
                    )
                    FileOutputStream(photoFile).use { output ->
                        val buffer = ByteArray(4 * 1024) // buffer size
                        while (true) {
                            val byteCount = input.read(buffer)
                            if (byteCount < 0) break
                            output.write(buffer, 0, byteCount)
                        }
                        output.flush()
                    }
                }
            } else {
                photoFile = null
            }
            _photoState.value = value
        }

    private val _photoState = MutableStateFlow<Uri?>(null)
    val photoState = _photoState.asStateFlow()

    fun addSpan(type: SpanType, start: Int, end: Int) {
        spansInternal.add(Span(type, start, end))
        _spans.value = mutableListOf<Span>().apply {
            addAll(spansInternal)
        }
    }

    fun onFinishClick(text: String) {
        _buttonsState.value = false
        val photo = photoFile
        val mode = mode ?: return
        if (photo != null) {
            diaryRepository.uploadPicture(photo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is BlogInResult.Success -> {
                            val pictureId = it.data
                            sendPost(
                                mode.isEdit,
                                text,
                                spansInternal,
                                !mode.isBlog,
                                Date().time,
                                pictureId
                            )
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось загрузить фото"
                        }
                    }
                    _buttonsState.value = true
                }, {
                    Timber.e(TAG, it)
                })
                .addTo(disposable)
        } else {
            val pictureId = if (userDeletedPhoto) null else mode.post?.pictureId
            sendPost(mode.isEdit, text, spansInternal, !mode.isBlog, Date().time, pictureId)
        }
    }

    fun savePhoto(url: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            saveImage(
                Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .submit()
                    .get(), context.cacheDir, "${Date().time}.jpg"
            )
        }
    }

    private fun saveImage(image: Bitmap, storageDir: File, imageFileName: String) {
        val imageFile = File(storageDir, imageFileName)
        try {
            val fOut: OutputStream = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
            photoUri = Uri.fromFile(imageFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendPost(
        isEdit: Boolean,
        text: String,
        spans: List<Span>,
        isPrivate: Boolean,
        dateTime: Long,
        pictureId: Long?
    ) {
        _buttonsState.value = false
        if (isEdit) {
            val id = mode!!.post!!.id
            diaryRepository.editPost(id, text, spans, isPrivate, dateTime, pictureId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is BlogInResult.Success -> {
                            router.pop()
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось отредактировать пост"
                        }
                    }
                    _buttonsState.value = true
                }, {
                    Timber.e(TAG, it)
                })
                .addTo(disposable)
        } else {
            diaryRepository.newPost(text, spans, isPrivate, dateTime, pictureId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is BlogInResult.Success -> {
                            router.pop()
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось создать пост"
                        }
                    }
                    _buttonsState.value = true
                }, {
                    Timber.e(TAG, it)
                })
                .addTo(disposable)
        }
    }

    fun addSpans(spans: List<Span>) {
        spansInternal.addAll(spans)
        _spans.value = mutableListOf<Span>().apply {
            addAll(spansInternal)
        }
    }

    fun back() {
        router.pop()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    companion object {
        private val TAG = PostViewModel::class.simpleName
    }
}