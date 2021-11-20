package ru.spbstu.profile.edit_profile.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.profile.edit_profile.presentation.EditProfileFragment

@Subcomponent(
    modules = [
        EditProfileModule::class,
    ]
)
@ScreenScope
interface EditProfileComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): EditProfileComponent
    }

    fun inject(editProfileFragment: EditProfileFragment)

}