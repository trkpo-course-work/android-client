package ru.spbstu.profile.profile.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.greenrobot.eventbus.EventBus
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.events.AuthEvent
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.profile.repository.ProfileRepository
import timber.log.Timber

class ProfileViewModel(
    private val router: ProfileRouter,
    private val profileRepository: ProfileRepository,

) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val disposable = CompositeDisposable()

    init {
        loadData()
    }

    fun editProfile() {
        router.navigateToProfileEdit()
    }

    fun openFavorites() {
        router.navigateToFavorites()
    }

    fun delete() {
        profileRepository.deleteUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _error.value = "Профиль удалён"
                        EventBus.getDefault().post(AuthEvent())
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Ошибка получения данных"
                    }
                }
            }, {
                Timber.d(TAG, "loadUserProfile: $it")
            })
            .addTo(disposable)
    }

    fun loadData() {
        profileRepository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Ошибка получения данных"
                    }
                }
            }, {
                Timber.d(TAG, "loadUserProfile: $it")
            })
            .addTo(disposable)
    }

    fun logout() {
        profileRepository.logout()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        EventBus.getDefault().post(AuthEvent())
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось выйти"
                    }
                }
            }, {
                Timber.d(TAG, "logout: $it")
            })
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    data class State(val profile: UserProfile)

    companion object {
        private val TAG = ProfileViewModel::class.simpleName
    }
}