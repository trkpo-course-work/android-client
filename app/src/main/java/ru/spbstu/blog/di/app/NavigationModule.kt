package ru.spbstu.blog.di.app

import dagger.Module
import dagger.Provides
import ru.spbstu.auth.AuthRouter
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.search.SearchRouter
import ru.spbstu.wall.WallRouter

@Module
class NavigationModule {

    @ApplicationScope
    @Provides
    fun provideNavigator(): Navigator = Navigator()

    @ApplicationScope
    @Provides
    fun provideAuthRouter(navigator: Navigator): AuthRouter = navigator

    @ApplicationScope
    @Provides
    fun provideWallRouter(navigator: Navigator): WallRouter = navigator

    @ApplicationScope
    @Provides
    fun provideSearchRouter(navigator: Navigator): SearchRouter = navigator

    @ApplicationScope
    @Provides
    fun provideProfileRouter(navigator: Navigator): ProfileRouter = navigator

    @ApplicationScope
    @Provides
    fun provideDiaryRouter(navigator: Navigator): DiaryRouter = navigator
}