package ru.spbstu.profile.favorites.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.profile.favorites.presentation.FavoritesFragment

@Subcomponent(
    modules = [
        FavoritesModule::class,
    ]
)
@ScreenScope
interface FavoritesComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): FavoritesComponent
    }

    fun inject(fragment: FavoritesFragment)

}