package ru.spbstu.diary.user_diary.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.repository.DiaryRepository
import ru.spbstu.diary.user_diary.presentation.UserDiaryViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class UserDiaryModule {
    @Provides
    @IntoMap
    @ViewModelKey(UserDiaryViewModel::class)
    fun provideViewModel(router: DiaryRouter, diaryRepository: DiaryRepository): ViewModel {
        return UserDiaryViewModel(router, diaryRepository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): UserDiaryViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(UserDiaryViewModel::class.java)
    }
}