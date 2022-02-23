package ru.spbstu.common.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val id: Long,
    val user: UserProfile,
    val text: String,
    val spans: List<Span>?,
    val isPrivate: Boolean,
    val dateTime: Long,
    val pictureId: Long?,
) : Parcelable
