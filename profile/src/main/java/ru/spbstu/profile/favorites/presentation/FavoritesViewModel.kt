package ru.spbstu.profile.favorites.presentation

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

class FavoritesViewModel(
    private val router: ProfileRouter,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val disposable = CompositeDisposable()

    fun loadData() {
        profileRepository.getUserSubscriptions()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when (it) {
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось получить избранное"
                    }
                }
            }, {
                Timber.d(TAG, "loadData: $it")
            })
            .addTo(disposable)
    }

    fun openUserProfile(userProfile: UserProfile) {
        router.openProfileFromFavorites(userProfile.id)
    }

    fun deleteFromFavorite(userProfile: UserProfile) {
        profileRepository.removeFromFavorite(userProfile.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                loadData()
            }, {
                Timber.d(TAG, "deleteFromFavorite: $it")
            })
            .addTo(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    class State(val favorites: List<UserProfile>)

    companion object {
        private val TAG = FavoritesViewModel::class.simpleName
    }
}