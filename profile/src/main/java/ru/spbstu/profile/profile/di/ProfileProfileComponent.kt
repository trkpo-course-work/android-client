package ru.spbstu.profile.profile.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.profile.profile.presentation.ProfileFragment

@Subcomponent(
    modules = [
        ProfileProfileModule::class,
    ]
)
@ScreenScope
interface ProfileProfileComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): ProfileProfileComponent
    }

    fun inject(authFragment: ProfileFragment)

}