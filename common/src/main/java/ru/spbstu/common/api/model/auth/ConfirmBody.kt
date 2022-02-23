package ru.spbstu.common.api.model.auth

import com.google.gson.annotations.SerializedName

data class ConfirmBody(
    @SerializedName("login") val login: String,
    @SerializedName("code") val code: String
)