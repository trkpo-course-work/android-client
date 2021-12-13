package ru.spbstu.wall.di

import dagger.Module
import dagger.Provides
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.wall.repository.WallRepository

@Module
class WallDataModule {
    @Provides
    @FeatureScope
    fun provideWallRepository(api: Api): WallRepository = WallRepository(api)
}