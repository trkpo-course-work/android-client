package ru.spbstu.common.tokens

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named

class TokensRepositoryImpl @Inject constructor(
    @Named("encrypted")
    private val sharedPreferences: SharedPreferences
) : TokensRepository {
    override fun getToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    override fun getRefresh(): RefreshToken? {
        val token = sharedPreferences.getString(REFRESH_KEY, null)
        return if (token != null) RefreshToken(token) else null
    }

    override fun saveRefresh(refresh: String) {
        sharedPreferences.edit().putString(REFRESH_KEY, refresh).apply()
    }

    override fun clearTokens() {
        sharedPreferences.edit().putString(TOKEN_KEY, null).apply()
        sharedPreferences.edit().putString(REFRESH_KEY, null).apply()
    }

    private companion object {
        private const val TOKEN_KEY = "studio.clapp.yarepetitor.TokenRepositoryImpl.token"
        private const val REFRESH_KEY = "studio.clapp.yarepetitor.TokenRepositoryImpl.refresh"
    }

}