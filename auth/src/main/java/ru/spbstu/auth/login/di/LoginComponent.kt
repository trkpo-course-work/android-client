package ru.spbstu.auth.login.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.auth.login.presentation.AuthFragment
import ru.spbstu.common.di.scope.ScreenScope

@Subcomponent(
    modules = [
        LoginModule::class,
    ]
)
@ScreenScope
interface LoginComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): LoginComponent
    }

    fun inject(authFragment: AuthFragment)

}