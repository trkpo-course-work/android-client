package ru.spbstu.auth.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.auth.AuthRouter
import ru.spbstu.auth.login.di.LoginComponent
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope

@Component(
    dependencies = [
        AuthDependencies::class,
    ],
    modules = [
        AuthModule::class,
        AuthDataModule::class
    ]
)
@FeatureScope
interface AuthComponent {

    fun loginComponentFactory(): LoginComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance featureRouter: AuthRouter,
            deps: AuthDependencies
        ): AuthComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface AuthDependenciesComponent : AuthDependencies
}