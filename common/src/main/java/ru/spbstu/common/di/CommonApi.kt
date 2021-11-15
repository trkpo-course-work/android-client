package ru.spbstu.common.di

import android.content.Context
import android.content.SharedPreferences

interface CommonApi {
    fun context(): Context

    fun provideSharedPreferences(): SharedPreferences
}
