package ru.spbstu.diary.diary.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.diary.presentation.DiaryViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class DiaryDiaryModule {
    @Provides
    @IntoMap
    @ViewModelKey(DiaryViewModel::class)
    fun provideViewModel(searchRouter: DiaryRouter): ViewModel {
        return DiaryViewModel(searchRouter)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): DiaryViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(DiaryViewModel::class.java)
    }
}