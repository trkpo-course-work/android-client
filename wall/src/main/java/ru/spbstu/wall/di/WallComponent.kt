package ru.spbstu.wall.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.wall.WallRouter
import ru.spbstu.wall.blog.di.BlogComponent

@Component(
    dependencies = [
        WallDependencies::class,
    ],
    modules = [
        WallModule::class,
        WallDataModule::class
    ]
)
@FeatureScope
interface WallComponent {

    fun blogComponentFactory(): BlogComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance featureRouter: WallRouter,
            deps: WallDependencies
        ): WallComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface WallDependenciesComponent : WallDependencies
}