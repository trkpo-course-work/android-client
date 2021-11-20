package ru.spbstu.diary.user_diary.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.diary.user_diary.presentation.UserDiaryFragment

@Subcomponent(
    modules = [
        UserDiaryModule::class,
    ]
)
@ScreenScope
interface UserDiaryComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): UserDiaryComponent
    }

    fun inject(fragment: UserDiaryFragment)

}