package ru.spbstu.common.api.model.auth

import com.google.gson.annotations.SerializedName

data class RequestResetBody(@SerializedName("email") val email: String)