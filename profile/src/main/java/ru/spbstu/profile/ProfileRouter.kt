package ru.spbstu.profile

interface ProfileRouter {
    fun navigateToProfileEdit()
    fun navigateToFavorites()
    fun pop()
    fun openProfileFromFavorites(id: Long)
}