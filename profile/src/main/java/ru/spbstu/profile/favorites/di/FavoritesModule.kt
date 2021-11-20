package ru.spbstu.profile.favorites.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.favorites.presentation.FavoritesViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class FavoritesModule {
    @Provides
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun provideViewModel(
        router: ProfileRouter
    ): ViewModel {
        return FavoritesViewModel(router)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): FavoritesViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(FavoritesViewModel::class.java)
    }
}