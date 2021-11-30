package ru.spbstu.common.api.model

import com.google.gson.annotations.SerializedName

data class SignInBody(
    @SerializedName("full_name") val name: String, @SerializedName("login") val login: String,
    @SerializedName("email") val email: String, @SerializedName("password") val password: String
)