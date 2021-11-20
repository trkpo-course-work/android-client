package ru.spbstu.common.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Blog(
    val id: Long,
    val name: String,
    val date: String,
    val post: String,
    val avatarUrl: String,
    val photoUrl: String?
) : Parcelable
