package ru.spbstu.wall.blog.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.wall.blog.presentation.BlogFragment

@Subcomponent(
    modules = [
        BlogModule::class,
    ]
)
@ScreenScope
interface BlogComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): BlogComponent
    }

    fun inject(authFragment: BlogFragment)

}