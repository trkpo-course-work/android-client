package ru.spbstu.common.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProfile(
    val id: Long,
    val name: String,
    val pictureId: Long?,
    val login: String,
    val email: String,
    val password: String?,
    val newPassword: String?,
): Parcelable