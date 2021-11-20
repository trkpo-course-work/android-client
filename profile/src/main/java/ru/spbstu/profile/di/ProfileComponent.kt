package ru.spbstu.profile.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.edit_profile.di.EditProfileComponent
import ru.spbstu.profile.favorites.di.FavoritesComponent
import ru.spbstu.profile.profile.di.ProfileProfileComponent
import ru.spbstu.profile.user_profile.di.UserProfileComponent

@Component(
    dependencies = [
        ProfileDependencies::class,
    ],
    modules = [
        ProfileModule::class,
        ProfileDataModule::class
    ]
)
@FeatureScope
interface ProfileComponent {

    fun profileProfileComponentFactory(): ProfileProfileComponent.Factory
    fun editProfileComponentFactory(): EditProfileComponent.Factory
    fun userProfileComponentFactory(): UserProfileComponent.Factory
    fun favoritesComponentFactory(): FavoritesComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance featureRouter: ProfileRouter,
            deps: ProfileDependencies
        ): ProfileComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface ProfileDependenciesComponent : ProfileDependencies
}