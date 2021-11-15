package ru.spbstu.blog.root.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.blog.root.presentation.di.RootActivityComponent
import ru.spbstu.blog.root.presentation.main.di.MainFragmentComponent
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope

@Component(
    dependencies = [
        RootDependencies::class
    ],
    modules = [
        RootFeatureModule::class,
    ]
)
@FeatureScope
interface RootComponent {

    fun rootActivityComponentFactory(): RootActivityComponent.Factory

    fun mainFragmentComponentFactory(): MainFragmentComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance navigator: Navigator,
            deps: RootDependencies
        ): RootComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface RootFeatureDependenciesComponent : RootDependencies
}
