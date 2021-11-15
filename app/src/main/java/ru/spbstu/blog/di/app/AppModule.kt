package ru.spbstu.blog.di.app

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.spbstu.blog.App
import ru.spbstu.common.di.scope.ApplicationScope

@Module
class AppModule {

    @ApplicationScope
    @Provides
    fun provideContext(application: App): Context {
        return application
    }
}
