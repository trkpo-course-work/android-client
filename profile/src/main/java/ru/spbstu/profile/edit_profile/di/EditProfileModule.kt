package ru.spbstu.profile.edit_profile.di

import android.content.ContentResolver
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.profile.ProfileRouter
import ru.spbstu.profile.edit_profile.presentation.EditProfileViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class EditProfileModule {
    @Provides
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    fun provideViewModel(
        router: ProfileRouter,
        contentResolver: ContentResolver
    ): ViewModel {
        return EditProfileViewModel(router, contentResolver)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory,
    ): EditProfileViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(EditProfileViewModel::class.java)
    }
}