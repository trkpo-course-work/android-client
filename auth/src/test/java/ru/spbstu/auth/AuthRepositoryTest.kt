package ru.spbstu.auth

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Response
import ru.spbstu.auth.domain.LoginError
import ru.spbstu.auth.domain.Tokens
import ru.spbstu.auth.repository.AuthRepository
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.auth.*
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.EMPTY_RESULT
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.common.tokens.RefreshToken
import ru.spbstu.common.tokens.TokensRepository

class AuthRepositoryTest {
    private lateinit var mockApi: Api
    private lateinit var mockTokenRepository: TokensRepository
    private lateinit var successfulTokensResponse: TokensResponse
    private lateinit var mockAuthRepository: AuthRepository

    private val correctLoginBody: LoginBody = LoginBody("correct", "correct")
    private val correctLogin = "correct"
    private val incorrectLogin = "incorrect"
    private val correctEmail = "correct email"
    private val incorrectEmail = "incorrect email"
    private val newPass = "new pass"
    private val name = "name"
    private val accessToken = "access token"
    private val refreshToken = "refresh token"

    @Before
    fun setUp() {
        mockApi = mock(Api::class.java)
        mockTokenRepository = mock(TokensRepository::class.java)
        successfulTokensResponse = TokensResponse(accessToken, refreshToken)
        mockAuthRepository = AuthRepository(mockApi, mockTokenRepository)
    }

    @Test
    fun login_with_correct_login() {
        val response = Single.just(Response.success(successfulTokensResponse))
        `when`(mockApi.login(correctLoginBody)).thenReturn(response)
        val loginResponse = mockAuthRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Success(Tokens(accessToken, refreshToken)), loginResponse)
        verify(mockTokenRepository).saveRefresh(refreshToken)
        verify(mockTokenRepository).saveToken(accessToken)
    }

    @Test
    fun login_with_incorrect_login_and_403_code() {
        val response = Single.just(Response.error<TokensResponse>(403, "".toResponseBody()))
        `when`(mockApi.login(correctLoginBody)).thenReturn(response)
        val loginResponse = mockAuthRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Error<Tokens>(LoginError.NOT_CONFIRMED), loginResponse)
    }

    @Test
    fun login_with_incorrect_login_and_not_403_code() {
        val response = Single.just(Response.error<TokensResponse>(500, "".toResponseBody()))
        `when`(mockApi.login(correctLoginBody)).thenReturn(response)
        val loginResponse = mockAuthRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Error<Tokens>(UNKNOWN_ERROR), loginResponse)
    }

    @Test
    fun logout_with_refresh_token() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockTokenRepository.getRefresh()).thenReturn(RefreshToken(refreshToken))
        `when`(mockApi.logout(RefreshToken(refreshToken))).thenReturn(response)
        val logoutResponse = mockAuthRepository.logout().blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), logoutResponse)
    }

    @Test
    fun logout_without_refresh_token() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockTokenRepository.getRefresh()).thenReturn(null)
        `when`(mockApi.logout(RefreshToken(""))).thenReturn(response)
        val logoutResponse = mockAuthRepository.logout().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), logoutResponse)
    }

    @Test
    fun requestConfirm_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.requestConfirmation(RequestConfirmationBody(correctLogin))).thenReturn(response)
        val requestConfirmResponse = mockAuthRepository.requestConfirm(correctLogin).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestConfirmResponse)
    }

    @Test
    fun requestConfirm_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.requestConfirmation(RequestConfirmationBody(incorrectLogin))).thenReturn(response)
        val requestConfirmResponse = mockAuthRepository.requestConfirm(incorrectLogin).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestConfirmResponse)
    }

    @Test
    fun confirm_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.confirm(ConfirmBody(correctLogin, "200"))).thenReturn(response)
        val confirmResponse = mockAuthRepository.confirm(correctLogin, "200").blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), confirmResponse)
    }

    @Test
    fun confirm_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.confirm(ConfirmBody(incorrectLogin, "403"))).thenReturn(response)
        val confirmResponse = mockAuthRepository.confirm(incorrectLogin, "403").blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), confirmResponse)
    }

    @Test
    fun requestReset_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.requestReset(RequestResetBody(correctEmail))).thenReturn(response)
        val requestResetResponse = mockAuthRepository.requestReset(correctEmail).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestResetResponse)
    }

    @Test
    fun requestReset_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.requestReset(RequestResetBody(incorrectEmail))).thenReturn(response)
        val requestResetResponse = mockAuthRepository.requestReset(incorrectEmail).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestResetResponse)
    }

    @Test
    fun checkReset_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.checkResetCode(CheckResetBody(correctEmail, "200"))).thenReturn(response)
        val checkResetResponse = mockAuthRepository.checkReset(correctEmail, "200").blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), checkResetResponse)
    }

    @Test
    fun checkReset_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.checkResetCode(CheckResetBody(incorrectEmail, "400"))).thenReturn(response)
        val checkResetResponse = mockAuthRepository.checkReset(incorrectEmail, "400").blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), checkResetResponse)
    }

    @Test
    fun reset_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.reset(ResetBody(correctEmail, "200", newPass))).thenReturn(response)
        val resetResponse = mockAuthRepository.reset(correctEmail, "200", newPass).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), resetResponse)
    }

    @Test
    fun reset_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.reset(ResetBody(incorrectEmail, "400", newPass))).thenReturn(response)
        val resetResponse = mockAuthRepository.reset(incorrectEmail, "400", newPass).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), resetResponse)
    }

    @Test
    fun signIn_wth_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.signIn(SignInBody(name, correctLogin, correctEmail, newPass))).thenReturn(response)
        val resetResponse = mockAuthRepository.signIn(name, correctLogin, correctEmail, newPass).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), resetResponse)
    }

    @Test
    fun signIn_wth_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.signIn(SignInBody(name, incorrectLogin, incorrectEmail, newPass))).thenReturn(response)
        val resetResponse = mockAuthRepository.signIn(name, incorrectLogin, incorrectEmail, newPass).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), resetResponse)
    }
}