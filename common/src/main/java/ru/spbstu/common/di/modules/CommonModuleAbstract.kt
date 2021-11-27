package ru.spbstu.common.di.modules

import dagger.Binds
import dagger.Module
import ru.spbstu.common.tokens.TokensRepository
import ru.spbstu.common.tokens.TokensRepositoryImpl

@Module
abstract class CommonModuleAbstract {

    @Binds
    abstract fun provideTokenRepository(tokenRepositoryImpl: TokensRepositoryImpl): TokensRepository
}