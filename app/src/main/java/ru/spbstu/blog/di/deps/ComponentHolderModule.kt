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

    /* @ApplicationScope
     @Binds
     @ClassKey(SplashFeatureApi::class)
     @IntoMap
     fun provideSplashFeatureHolder(splashFeatureHolder: SplashFeatureHolder): FeatureApiHolder*/
}