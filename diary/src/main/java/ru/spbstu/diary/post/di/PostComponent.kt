package ru.spbstu.diary.post.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.diary.post.presentation.PostFragment

@Subcomponent(
    modules = [
        PostModule::class,
    ]
)
@ScreenScope
interface PostComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): PostComponent
    }

    fun inject(fragment: PostFragment)

}