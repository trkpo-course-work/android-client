package ru.spbstu.common.domain

import ru.spbstu.common.api.model.blog.BlogPostBody
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.api.model.toDomainModel

fun UserResponse.toDomainModel(): UserProfile =
    UserProfile(id, name, pictureId, login, email, password, newPassword)

fun UserProfile.toNetworkModel(): UserResponse =
    UserResponse(id, name, pictureId, login, email, password, newPassword)

fun ru.spbstu.common.api.model.blog.Blog.toDomainModel(): Blog =
    Blog(
        id,
        userDTO.toDomainModel(),
        text,
        span?.map { it.toDomainModel() },
        private,
        dateTime,
        pictureId
    )

fun Span.toNetworkModel(): ru.spbstu.common.api.model.Span =
    ru.spbstu.common.api.model.Span(type.name, start, end)

fun Blog.toNetworkModel(): BlogPostBody =
    BlogPostBody(
        id,
        text,
        spans?.map { it.toNetworkModel() } ?: emptyList(),
        isPrivate,
        dateTime,
        pictureId)