package ru.spbstu.common.di

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences

interface CommonApi {
    fun context(): Context

    fun provideSharedPreferences(): SharedPreferences

    fun contentResolver(): ContentResolver
}
