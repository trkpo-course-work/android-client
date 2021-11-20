package ru.spbstu.search.search.di

import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import ru.spbstu.common.di.scope.ScreenScope
import ru.spbstu.search.search.presentation.SearchFragment

@Subcomponent(
    modules = [
        SearchSearchModule::class,
    ]
)
@ScreenScope
interface SearchSearchComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): SearchSearchComponent
    }

    fun inject(authFragment: SearchFragment)

}