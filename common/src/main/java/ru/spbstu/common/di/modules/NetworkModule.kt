package ru.spbstu.common.di.modules

import dagger.Module

@Module
class NetworkModule {

    companion object {
        private val TAG = NetworkModule::class.simpleName
        private const val BEARER = "Bearer "
        private const val AUTHORIZATION = "Authorization"
    }
}
