package ru.spbstu.profile.favorites.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.favorites.presentation.FavoritesViewModel
import ru.spbstu.profile.repository.ProfileRepository
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
        router: ProfileRouter,
        repository: ProfileRepository,
    ): ViewModel {
        return FavoritesViewModel(router, repository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): FavoritesViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(FavoritesViewModel::class.java)
    }
}