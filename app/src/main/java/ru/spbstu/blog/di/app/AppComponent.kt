package ru.spbstu.blog.di.app

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.blog.App
import ru.spbstu.blog.di.deps.ComponentHolderModule
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.modules.CommonModule
import ru.spbstu.common.di.modules.NetworkModule
import ru.spbstu.common.di.scope.ApplicationScope

@ApplicationScope
@Component(
    modules = [
        AppModule::class,
        CommonModule::class,
        NetworkModule::class,
        NavigationModule::class,
        ComponentHolderModule::class,
        FeatureManagerModule::class
    ]
)
interface AppComponent : CommonApi {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
}