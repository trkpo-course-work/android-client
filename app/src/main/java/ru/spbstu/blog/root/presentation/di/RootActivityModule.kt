package ru.spbstu.blog.root.presentation.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.spbstu.blog.navgiation.Navigator
import ru.spbstu.blog.root.presentation.RootActivityViewModel
import ru.spbstu.blog.root.presentation.RootRouter
import studio.clapp.common.di.viewmodel.ViewModelKey
import studio.clapp.common.di.viewmodel.ViewModelModule

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class RootActivityModule {

    @Provides
    fun provideRootRouter(navigator: Navigator): RootRouter = navigator

    @Provides
    @IntoMap
    @ViewModelKey(RootActivityViewModel::class)
    fun provideViewModel(
        rootRouter: RootRouter,
    ): ViewModel {
        return RootActivityViewModel(
            rootRouter
        )
    }

    @Provides
    fun provideViewModelCreator(
        activity: AppCompatActivity,
        viewModelFactory: ViewModelProvider.Factory
    ): RootActivityViewModel {
        return ViewModelProvider(activity, viewModelFactory).get(RootActivityViewModel::class.java)
    }
}