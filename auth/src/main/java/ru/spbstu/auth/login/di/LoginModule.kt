package ru.spbstu.auth.login.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.auth.AuthRouter
import ru.spbstu.auth.login.presentation.AuthViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class LoginModule {
    @Provides
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    fun provideViewModel(authRouter: AuthRouter): ViewModel {
        return AuthViewModel(authRouter)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): AuthViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(AuthViewModel::class.java)
    }
}