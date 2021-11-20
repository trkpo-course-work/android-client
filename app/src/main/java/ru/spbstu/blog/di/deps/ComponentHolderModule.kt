package ru.spbstu.blog.di.deps

import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import ru.spbstu.auth.di.AuthApi
import ru.spbstu.auth.di.AuthFeatureHolder
import ru.spbstu.blog.App
import ru.spbstu.blog.root.di.RootApi
import ru.spbstu.blog.root.di.RootFeatureHolder
import ru.spbstu.common.di.FeatureApiHolder
import ru.spbstu.common.di.FeatureContainer
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.diary.di.DiaryApi
import ru.spbstu.diary.di.DiaryFeatureHolder
import ru.spbstu.profile.di.ProfileApi
import ru.spbstu.profile.di.ProfileFeatureHolder
import ru.spbstu.search.di.SearchApi
import ru.spbstu.search.di.SearchFeatureHolder
import ru.spbstu.wall.di.WallApi
import ru.spbstu.wall.di.WallFeatureHolder

@Module
interface ComponentHolderModule {

    @ApplicationScope
    @Binds
    fun provideFeatureContainer(application: App): FeatureContainer

    @ApplicationScope
    @Binds
    @ClassKey(RootApi::class)
    @IntoMap
    fun provideRootFeatureHolder(rootFeatureHolder: RootFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(AuthApi::class)
    @IntoMap
    fun provideAuthFeatureHolder(authFeatureHolder: AuthFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(WallApi::class)
    @IntoMap
    fun provideWallFeatureHolder(wallFeatureHolder: WallFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(SearchApi::class)
    @IntoMap
    fun provideSearchFeatureHolder(wallFeatureHolder: SearchFeatureHolder): FeatureApiHolder

    @ApplicationScope
    @Binds
    @ClassKey(ProfileApi::class)
    @IntoMap
    fun provideProfileFeatureHolder(profileFeatureHolder: ProfileFeatureHolder): FeatureApiHolder

     @ApplicationScope
     @Binds
     @ClassKey(DiaryApi::class)
     @IntoMap
     fun provideDiaryFeatureHolder(splashFeatureHolder: DiaryFeatureHolder): FeatureApiHolder
}