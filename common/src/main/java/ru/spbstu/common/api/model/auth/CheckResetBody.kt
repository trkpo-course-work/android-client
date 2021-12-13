package ru.spbstu.common.api.model.auth

import com.google.gson.annotations.SerializedName

data class CheckResetBody(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String
)