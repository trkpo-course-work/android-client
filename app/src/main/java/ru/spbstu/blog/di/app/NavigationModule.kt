package ru.spbstu.blog.di.app

import dagger.Module
import dagger.Provides
import ru.spbstu.auth.AuthRouter
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.common.di.scope.ApplicationScope

@Module
class NavigationModule {

    @ApplicationScope
    @Provides
    fun provideNavigator(): Navigator = Navigator()

    @ApplicationScope
    @Provides
    fun provideAuthRouter(navigator: Navigator): AuthRouter = navigator
}