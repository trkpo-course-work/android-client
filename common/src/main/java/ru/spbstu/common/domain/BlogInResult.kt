package ru.spbstu.common.domain

sealed class BlogInResult<T> {
    data class Success<T>(val data: T): BlogInResult<T>()
    data class Error<T>(val error: ErrorEntity): BlogInResult<T>()
}

interface ErrorEntity

object UNKNOWN_ERROR: ErrorEntity
object EMPTY_RESULT