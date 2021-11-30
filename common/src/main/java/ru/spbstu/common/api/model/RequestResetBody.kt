package ru.spbstu.common.api.model

import com.google.gson.annotations.SerializedName

data class RequestResetBody(@SerializedName("email") val email: String)