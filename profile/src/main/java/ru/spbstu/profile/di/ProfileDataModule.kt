package ru.spbstu.profile.di

import dagger.Module
import dagger.Provides
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.common.tokens.TokensRepository
import ru.spbstu.profile.repository.ProfileRepository

@Module
class ProfileDataModule {
    @Provides
    @FeatureScope
    fun provideProfileRepository(api: Api, tokensRepository: TokensRepository): ProfileRepository =
        ProfileRepository(api, tokensRepository)
}