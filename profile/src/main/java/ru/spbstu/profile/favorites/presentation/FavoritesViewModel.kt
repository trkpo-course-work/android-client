package ru.spbstu.profile.favorites.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.favorites.domain.Favorite

class FavoritesViewModel(private val router: ProfileRouter) : ViewModel() {

    private val _state = MutableStateFlow<State?>(null)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _state.value = State(
            listOf(
                Favorite(1, "artur1"),
                Favorite(2, "artur2"),
                Favorite(3, "artur3"),
            )
        )
    }

    class State(val favorites: List<Favorite>)
}