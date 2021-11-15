package ru.spbstu.common.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import ru.spbstu.common.di.scope.ApplicationScope

const val SHARED_PREFERENCES_FILE = "ru.spbstu.blogin.preferences"

@Module
class CommonModule {
    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }
}
