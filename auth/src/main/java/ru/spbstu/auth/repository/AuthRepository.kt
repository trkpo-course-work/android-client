package ru.spbstu.auth.repository

import io.reactivex.rxjava3.core.Single
import ru.spbstu.auth.domain.LoginError
import ru.spbstu.auth.domain.Tokens
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.auth.*
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.EMPTY_RESULT
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.common.tokens.RefreshToken
import ru.spbstu.common.tokens.TokensRepository

class AuthRepository(private val api: Api, private val tokensRepository: TokensRepository) {
    fun login(login: String, password: String): Single<BlogInResult<Tokens>> {
        return api.login(LoginBody(login, password))
            .map {
                if (it.isSuccessful) {
                    val access = it.body()?.accessToken
                    val refresh = it.body()?.refreshToken
                    if (access == null || refresh == null) {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    } else {
                        tokensRepository.saveRefresh(refresh)
                        tokensRepository.saveToken(access)
                        BlogInResult.Success(Tokens(access, refresh))
                    }
                } else {
                    if (it.code() == 403) {
                        BlogInResult.Error(LoginError.NOT_CONFIRMED)
                    } else {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }


    fun logout(): Single<BlogInResult<Any>> {
        val refreshToken = tokensRepository.getRefresh()?.refreshToken ?: ""
        return api.logout(RefreshToken(refreshToken))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }


    fun requestConfirm(login: String): Single<BlogInResult<Any>> {
        return api.requestConfirmation(RequestConfirmationBody(login))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

    fun confirm(login: String, code: String): Single<BlogInResult<Any>> {
        return api.confirm(ConfirmBody(login, code))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

    fun requestReset(email: String): Single<BlogInResult<Any>> {
        return api.requestReset(RequestResetBody(email))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

    fun checkReset(email: String, code: String): Single<BlogInResult<Any>> {
        return api.checkResetCode(CheckResetBody(email, code))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

    fun reset(email: String, code: String, newPass: String): Single<BlogInResult<Any>> {
        return api.reset(ResetBody(email, code, newPass))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

    fun signIn(
        name: String,
        login: String,
        email: String,
        password: String
    ): Single<BlogInResult<Any>> {
        return api.signIn(SignInBody(name, login, email, password))
            .map {
                when {
                    it.isSuccessful -> {
                        BlogInResult.Success(EMPTY_RESULT)
                    }
                    else -> {
                        BlogInResult.Error(UNKNOWN_ERROR)
                    }
                }
            }
    }

}