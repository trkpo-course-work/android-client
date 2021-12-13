package ru.spbstu.common.api.model.auth

import com.google.gson.annotations.SerializedName

data class RequestConfirmationBody(@SerializedName("login") val login: String)