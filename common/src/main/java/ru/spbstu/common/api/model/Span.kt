package ru.spbstu.common.api.model


data class Span(val type: String, val start: Int, val end: Int)

enum class SpanType {
    BOLD,
    ITALIC,
    UNDERLINE,
}

fun String.toSpanType(): SpanType {
    return when (this) {
        SpanType.BOLD.name -> SpanType.BOLD
        SpanType.ITALIC.name -> SpanType.ITALIC
        SpanType.UNDERLINE.name -> SpanType.UNDERLINE
        else -> throw IllegalArgumentException("Unknown SpanType")
    }
}

fun Span.toDomainModel(): ru.spbstu.common.domain.Span = ru.spbstu.common.domain.Span(type.toSpanType(), start, end)
