package ru.spbstu.common.api

import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.spbstu.common.api.model.*
import ru.spbstu.common.tokens.RefreshToken

interface Api {
    @POST("/api/v1/auth/login")
    fun login(@Body loginBody: LoginBody): Single<Response<TokensResponse>>

    @POST("/api/v1/auth/logout")
    fun logout(@Body refreshToken: RefreshToken): Single<Response<Void>>

    @POST("/api/v1/auth/request_confirmation")
    fun requestConfirmation(@Body requestConfirmationBody: RequestConfirmationBody): Single<Response<Void>>

    @POST("/api/v1/auth/confirm")
    fun confirm(@Body confirmBody: ConfirmBody): Single<Response<Void>>

    @POST("/api/v1/auth/request_reset")
    fun requestReset(@Body requestResetBody: RequestResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/check_reset_code")
    fun checkResetCode(@Body checkResetBody: CheckResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/reset")
    fun reset(resetBody: ResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/signup")
    fun signIn(@Body signInBody: SignInBody): Single<Response<Void>>
}