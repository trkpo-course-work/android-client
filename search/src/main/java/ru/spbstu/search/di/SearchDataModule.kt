package ru.spbstu.search.di

import dagger.Module
import dagger.Provides
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.search.repository.SearchRepository

@Module
class SearchDataModule {
    @Provides
    @FeatureScope
    fun provideSearchRepository(api: Api): SearchRepository =
        SearchRepository(api)
}