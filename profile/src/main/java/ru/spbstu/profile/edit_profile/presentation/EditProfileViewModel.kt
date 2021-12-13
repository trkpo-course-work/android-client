package ru.spbstu.profile.edit_profile.presentation

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.profile.repository.ProfileRepository
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class EditProfileViewModel(
    private val router: ProfileRouter,
    private val contentResolver: ContentResolver,
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()
    private var photoFile: File? = null

    private val _uriPhotoState = MutableStateFlow<Uri?>(null)
    val uriPhotoState = _uriPhotoState.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val disposable = CompositeDisposable()

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
                _uriPhotoState.value = Uri.fromFile(photoFile)
            }
        }

    init {
        loadData()
    }


    fun editProfile(
        name: String,
        login: String,
        oldPass: String?,
        newPass: String?,
        passChange: Boolean
    ) {
        val file = photoFile
        val isChangePhoto = file != null
        val id = state.value?.profile?.id ?: return
        val pictureId = state.value?.profile?.pictureId
        val email = state.value?.profile?.email ?: return
        if (isChangePhoto && file != null) {
            profileRepository.uploadPicture(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        is BlogInResult.Success -> {
                            editProfile(id, name, login, email, oldPass, newPass, passChange, it.data)
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось загрузить фото"
                        }
                    }
                }, {
                    Timber.e(TAG, it)
                })
                .addTo(disposable)
        } else {
            editProfile(id, name, login, email, oldPass, newPass, passChange, pictureId)
        }
    }

    private fun editProfile(
        id: Long,
        name: String,
        login: String,
        email: String,
        oldPass: String?,
        newPass: String?,
        passChange: Boolean,
        pictureId: Long?,
    ) {
        profileRepository.editProfile(
            UserProfile(
                id,
                name,
                pictureId,
                login,
                email,
                if (passChange) oldPass else null,
                if (passChange) newPass else null
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        router.pop()
                        _error.value = "Данные успешно изменены"
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Ошибка изменения данных"
                    }
                }
            }, {
                Timber.d(TAG, "editProfile: $it")
            })
            .addTo(disposable)
    }

    fun onNavigationIconClick() {
        router.pop()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    private fun loadData() {
        profileRepository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Ошибка входа"
                    }
                }
            }, {
                Timber.d(TAG, "loadUserProfile: $it")
            })
            .addTo(disposable)
    }

    data class State(val profile: UserProfile)

    companion object {
        private val TAG = EditProfileViewModel::class.simpleName
    }
}