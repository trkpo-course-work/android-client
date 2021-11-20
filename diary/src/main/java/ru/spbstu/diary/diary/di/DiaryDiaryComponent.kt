package ru.spbstu.diary.diary.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.diary.diary.presentation.DiaryFragment

@Subcomponent(
    modules = [
        DiaryDiaryModule::class,
    ]
)
@ScreenScope
interface DiaryDiaryComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): DiaryDiaryComponent
    }

    fun inject(fragment: DiaryFragment)

}