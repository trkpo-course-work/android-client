package ru.spbstu.profile.edit_profile.presentation

import android.content.ContentResolver
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.profile.domain.Profile
import java.io.File
import java.io.FileOutputStream

class EditProfileViewModel(
    private val router: ProfileRouter,
    private val contentResolver: ContentResolver
) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()
    private var photoFile: File? = null

    private val _uriPhotoState = MutableStateFlow<Uri?>(null)
    val uriPhotoState = _uriPhotoState.asStateFlow()

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

    fun onNavigationIconClick() {
        router.pop()
    }

    private fun loadData() {
        _state.value = State(
            Profile(1, "Artur", "viruskuls", null)
        )
    }

    data class State(val profile: Profile)
}