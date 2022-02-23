package ru.spbstu.auth.di

import dagger.Module
import dagger.Provides
import ru.spbstu.auth.repository.AuthRepository
import ru.spbstu.common.api.Api
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.common.tokens.TokensRepository

@Module
class AuthDataModule {
    @Provides
    @FeatureScope
    fun provideAuthRepository(api: Api, tokensRepository: TokensRepository): AuthRepository =
        AuthRepository(api, tokensRepository)
}