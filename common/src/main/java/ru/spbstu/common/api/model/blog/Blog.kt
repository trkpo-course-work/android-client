package ru.spbstu.common.api.model.blog

import ru.spbstu.common.api.model.Span
import ru.spbstu.common.api.model.profile.UserResponse

data class Blog(
    val id: Long,
    val userDTO: UserResponse,
    val text: String,
    val span: List<Span>?,
    val private: Boolean,
    val dateTime: Long,
    val pictureId: Long?
)