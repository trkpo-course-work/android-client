package ru.spbstu.diary.user_blog.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.diary.user_blog.presentation.UserBlogFragment

@Subcomponent(
    modules = [
        UserBlogModule::class,
    ]
)
@ScreenScope
interface UserBlogComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): UserBlogComponent
    }

    fun inject(fragment: UserBlogFragment)

}