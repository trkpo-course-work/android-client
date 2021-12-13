package ru.spbstu.profile.user_profile.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.repository.ProfileRepository
import timber.log.Timber

class UserProfileViewModel(
    private val router: ProfileRouter,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    private val _posts = MutableStateFlow<List<Blog>?>(null)
    val posts = _posts.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isFavorite = MutableStateFlow<Boolean?>(null)
    val isFavorite = _isFavorite.asStateFlow()

    private val disposable = CompositeDisposable()

    var userId = -1L
        set(value) {
            if (field != value) {
                field = value
                loadData()
            }
        }

    private fun loadData() {
        profileRepository.getUserInfo(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it){
                    is BlogInResult.Success -> {
                        _state.value = State(it.data)
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось получить информацию профиля"
                    }
                }
            }, {
                Timber.d(TAG, "loadData: $it")
            })

        profileRepository.getUserPosts(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it){
                    is BlogInResult.Success -> {
                        _posts.value = it.data
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось получить посты пользователя"
                    }
                }
            }, {
                Timber.d(TAG, "loadData: $it")
            })

        loadFavorite()
    }

    private fun loadFavorite() {
        profileRepository.isUserFavorite(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                when(it){
                    is BlogInResult.Success -> {
                        _isFavorite.value = it.data
                    }
                    is BlogInResult.Error -> {
                        _error.value = "Не удалось узнать в избранном ли пользователь"
                    }
                }
            }, {
                Timber.d(TAG, "loadFavorite: $it")
            })
    }

    fun changeFavoriteState() {
        val current = _isFavorite.value ?: return
        if (!current) {
            profileRepository.addToFavorite(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it){
                        is BlogInResult.Success -> {
                            loadFavorite()
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось добавить в избранное"
                        }
                    }
                }, {
                    Timber.d(TAG, "changeFavoriteState: $it")
                })
        } else {
            profileRepository.removeFromFavorite(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when(it){
                        is BlogInResult.Success -> {
                            loadFavorite()
                        }
                        is BlogInResult.Error -> {
                            _error.value = "Не удалось удалить из избранного"
                        }
                    }
                }, {
                    Timber.d(TAG, "changeFavoriteState: $it")
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    data class State(val profile: UserProfile)

    companion object {
        private val TAG = UserProfileViewModel::class.simpleName
    }
}