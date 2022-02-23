package ru.spbstu.common.api.model.blog

import ru.spbstu.common.api.model.Span

data class BlogPostBody(
    val id: Long?,
    val text: String,
    val span: List<Span>,
    val private: Boolean,
    val dateTime: Long,
    val pictureId: Long?
)