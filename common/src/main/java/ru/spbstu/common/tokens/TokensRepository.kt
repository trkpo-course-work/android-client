package ru.spbstu.common.tokens

interface TokensRepository {
    fun getToken(): String?
    fun saveToken(token: String)
    fun getRefresh(): RefreshToken?
    fun saveRefresh(refresh: String)
    fun clearTokens()
}