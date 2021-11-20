package ru.spbstu.profile.di

import dagger.Module

@Module
abstract class ProfileDataModule {
    /*@Binds
    @FeatureScope
    abstract fun bindFeatureRepository(featureRepositoryImpl: FeatureRepositoryImpl): FeatureRepository

    @Binds
    @FeatureScope
    abstract fun bindFeatureDataSource(featureDataSourceImpl: FeatureDataSourceImpl): FeatureDataSource

    companion object {
        @Provides
        @FeatureScope
        fun provideFeatureApiService(retrofit: Retrofit): FeatureApiService =
            retrofit.create(FeatureApiService::class.java)

        @Provides
        @FeatureScope
        fun provideDataWrapper(): FinancesClassesDataWrapper {
            return FinancesClassesDataWrapper()
        }
    }*/
}