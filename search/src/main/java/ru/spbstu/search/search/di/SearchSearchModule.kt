package ru.spbstu.search.search.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.search.SearchRouter
import ru.spbstu.search.repository.SearchRepository
import ru.spbstu.search.search.presentation.SearchViewModel
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class,
    ]
)
class SearchSearchModule {
    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideViewModel(searchRouter: SearchRouter, searchRepository: SearchRepository): ViewModel {
        return SearchViewModel(searchRouter, searchRepository)
    }

    @Provides
    fun provideViewModelCreator(
        fragment: Fragment,
        viewModelFactory: ViewModelProvider.Factory
    ): SearchViewModel {
        return ViewModelProvider(fragment, viewModelFactory).get(SearchViewModel::class.java)
    }
}