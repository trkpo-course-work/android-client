package ru.spbstu.profile.profile.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.profile.domain.Profile

class ProfileViewModel(private val router: ProfileRouter) : ViewModel() {
    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun editProfile() {
        router.navigateToProfileEdit()
    }

    fun openFavorites() {
        router.navigateToFavorites()
    }

    private fun loadData() {
        _state.value = State(
            Profile(1, "Artur","viruskuls", null)
        )
    }

    data class State(val profile: Profile)
}