package ru.spbstu.common.api.model.auth

import com.google.gson.annotations.SerializedName

data class ResetBody(
    @SerializedName("email") val email: String,
    @SerializedName("code") val code: String,
    @SerializedName("password") val newPass: String
)