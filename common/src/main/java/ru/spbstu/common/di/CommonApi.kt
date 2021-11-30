package ru.spbstu.common.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import ru.spbstu.common.api.Api
import ru.spbstu.common.tokens.TokensRepository

interface CommonApi {
    fun context(): Context

    fun provideSharedPreferences(): SharedPreferences

    fun contentResolver(): ContentResolver

    fun provideApi(): Api

    fun tokensRepository(): TokensRepository
}
