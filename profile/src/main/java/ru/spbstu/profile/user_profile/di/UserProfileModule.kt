package ru.spbstu.profile.user_profile.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.repository.ProfileRepository
import ru.spbstu.profile.user_profile.presentation.UserProfileViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class UserProfileModule {
    @Provides
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    fun provideViewModel(
        searchRouter: ProfileRouter,
        profileRepository: ProfileRepository
    ): ViewModel {
        return UserProfileViewModel(searchRouter, profileRepository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): UserProfileViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(UserProfileViewModel::class.java)
    }
}