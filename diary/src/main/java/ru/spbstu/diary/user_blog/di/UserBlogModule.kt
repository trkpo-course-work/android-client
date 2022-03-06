package ru.spbstu.diary.user_blog.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.repository.DiaryRepository
import ru.spbstu.diary.user_blog.presentation.UserBlogViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class UserBlogModule {
    @Provides
    @IntoMap
    @ViewModelKey(UserBlogViewModel::class)
    fun provideViewModel(router: DiaryRouter, diaryRepository: DiaryRepository): ViewModel {
        return UserBlogViewModel(router, diaryRepository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): UserBlogViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(UserBlogViewModel::class.java)
    }
}