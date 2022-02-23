package ru.spbstu.common.api.model.profile

data class UserResponse(
    val id: Long,
    val name: String,
    val pictureId: Long?,
    val login: String,
    val email: String,
    val password: String? = null,
    val newPassword: String? = null,
)