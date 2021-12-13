package ru.spbstu.wall.blog.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.wall.WallRouter
import ru.spbstu.wall.blog.presentation.BlogViewModel
import ru.spbstu.wall.repository.WallRepository
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class BlogModule {
    @Provides
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    fun provideViewModel(wallRouter: WallRouter, wallRepository: WallRepository): ViewModel {
        return BlogViewModel(wallRouter, wallRepository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): BlogViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(BlogViewModel::class.java)
    }
}