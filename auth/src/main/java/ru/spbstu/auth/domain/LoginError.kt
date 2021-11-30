package ru.spbstu.auth.domain

import ru.spbstu.common.domain.ErrorEntity

sealed class LoginError : ErrorEntity {
    object NOT_CONFIRMED : LoginError()
}