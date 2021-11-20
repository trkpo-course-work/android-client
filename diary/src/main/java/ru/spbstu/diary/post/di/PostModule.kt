package ru.spbstu.diary.post.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.post.presentation.PostViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class PostModule {
    @Provides
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    fun provideViewModel(router: DiaryRouter): ViewModel {
        return PostViewModel(router)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): PostViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(PostViewModel::class.java)
    }
}