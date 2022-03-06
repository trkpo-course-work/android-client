package ru.spbstu.diary.di

import dagger.Module
import dagger.Provides
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.diary.repository.DiaryRepository

@Module
class DiaryDataModule {
    @Provides
    @FeatureScope
    fun provideDiaryRepository(api: Api): DiaryRepository =
        DiaryRepository(api)
}