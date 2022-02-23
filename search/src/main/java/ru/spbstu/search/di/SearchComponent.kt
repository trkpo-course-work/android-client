package ru.spbstu.search.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.search.SearchRouter
import ru.spbstu.search.search.di.SearchSearchComponent

@Component(
    dependencies = [
        SearchDependencies::class,
    ],
    modules = [
        SearchModule::class,
        SearchDataModule::class
    ]
)
@FeatureScope
interface SearchComponent {

    fun searchSearchComponentFactory(): SearchSearchComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance featureRouter: SearchRouter,
            deps: SearchDependencies
        ): SearchComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface SearchDependenciesComponent : SearchDependencies
}