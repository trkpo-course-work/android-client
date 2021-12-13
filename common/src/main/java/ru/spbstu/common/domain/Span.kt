package ru.spbstu.common.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.spbstu.common.api.model.SpanType

@Parcelize
data class Span(val type: SpanType, val start: Int, val end: Int) : Parcelable