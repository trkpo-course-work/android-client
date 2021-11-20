package ru.spbstu.profile.user_profile.domain

import ru.spbstu.common.domain.Blog

data class UserProfile(
    val id: Long,
    val name: String,
    val isFavorite: Boolean,
    val avatarUrl: String?,
    val posts: List<Blog>
)