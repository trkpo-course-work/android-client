package ru.spbstu.profile

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.blog.Blog
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.EMPTY_RESULT
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.common.tokens.RefreshToken
import ru.spbstu.common.tokens.TokensRepository
import ru.spbstu.profile.repository.ProfileRepository

class ProfileRepositoryTest {
    private lateinit var mockApi: Api
    private lateinit var mockProfileRepository: ProfileRepository
    private lateinit var mockTokenRepository: TokensRepository

    private val userResponseApi: UserResponse = UserResponse(1, "name", null, "login", "email", "pass", null)
    private val userResponseDomain: UserProfile = UserProfile(1, "name", null, "login", "email", "pass", null)
    private val userResponse: UserResponse = UserResponse(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val userProfile: UserProfile = UserProfile(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val apiBlog: Blog = Blog(1, userResponse, "text", null, false, 123456, 1)
    private val domainBlog: ru.spbstu.common.domain.Blog =
        ru.spbstu.common.domain.Blog(1, userProfile, "text", null, false, 123456, 1)
    private val refreshToken: RefreshToken = RefreshToken("refresh token")

    @Before
    fun setUp() {
        mockApi = mock(Api::class.java)
        mockTokenRepository = mock(TokensRepository::class.java)
        mockProfileRepository = ProfileRepository(mockApi, mockTokenRepository)
    }

    @Test
    fun getUser_with_correct_response() {
        val response = Single.just(Response.success(userResponseApi))
        `when`(mockApi.getUser()).thenReturn(response)
        val requestGetUser = mockProfileRepository.getUser().blockingGet()
        assertEquals(BlogInResult.Success(userResponseDomain), requestGetUser)
    }

    @Test
    fun getUser_with_incorrect_response() {
        val response = Single.just(Response.error<UserResponse>(500, "".toResponseBody()))
        `when`(mockApi.getUser()).thenReturn(response)
        val requestGetUser = mockProfileRepository.getUser().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetUser)
    }

    @Test
    fun logout_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockTokenRepository.getRefresh()).thenReturn(RefreshToken("refresh token"))
        `when`(mockApi.logout(refreshToken)).thenReturn(response)
        val requestLogout = mockProfileRepository.logout().blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestLogout)
    }

    @Test
    fun logout_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockTokenRepository.getRefresh()).thenReturn(refreshToken)
        `when`(mockApi.logout(refreshToken)).thenReturn(response)
        val requestLogout = mockProfileRepository.logout().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestLogout)
    }

    @Test
    fun getUserSubscriptions_with_correct_response() {
        val userList = listOf(userResponseApi)
        val response = Single.just(Response.success(userList))
        `when`(mockApi.getUserSubscriptions()).thenReturn(response)
        val requestGetUserSubscriptions = mockProfileRepository.getUserSubscriptions().blockingGet()
        assertEquals(BlogInResult.Success(listOf(userResponseDomain)), requestGetUserSubscriptions)
    }

    @Test
    fun getUserSubscriptions_with_incorrect_response() {
        val response = Single.just(Response.error<List<UserResponse>>(500, "".toResponseBody()))
        `when`(mockApi.getUserSubscriptions()).thenReturn(response)
        val requestGetUserSubscriptions = mockProfileRepository.getUserSubscriptions().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetUserSubscriptions)
    }

    @Test
    fun editProfile_with_correct_response() {
        val response = Single.just(Response.success(userResponseApi))
        `when`(mockApi.editUser(userResponseApi)).thenReturn(response)
        val requestEditProfile = mockProfileRepository.editProfile(userResponseDomain).blockingGet()
        assertEquals(BlogInResult.Success(userResponseDomain), requestEditProfile)
    }

    @Test
    fun editProfile_with_incorrect_response() {
        val response = Single.just(Response.error<UserResponse>(500, "".toResponseBody()))
        `when`(mockApi.editUser(userResponseApi)).thenReturn(response)
        val requestEditProfile = mockProfileRepository.editProfile(userResponseDomain).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestEditProfile)
    }

    @Test
    fun getUserInfo_with_correct_response() {
        val response = Single.just(Response.success(userResponseApi))
        `when`(mockApi.getUserById(1)).thenReturn(response)
        val requestGetUserInfo = mockProfileRepository.getUserInfo(1).blockingGet()
        assertEquals(BlogInResult.Success(userResponseDomain), requestGetUserInfo)
    }

    @Test
    fun getUserInfo_with_incorrect_response() {
        val response = Single.just(Response.error<UserResponse>(500, "".toResponseBody()))
        `when`(mockApi.getUserById(1)).thenReturn(response)
        val requestGetUserInfo = mockProfileRepository.getUserInfo(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetUserInfo)
    }

    @Test
    fun uploadPicture() {
    }

    @Test
    fun deleteUser_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.deleteUser()).thenReturn(response)
        val requestDeleteUser = mockProfileRepository.deleteUser().blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestDeleteUser)
    }

    @Test
    fun deleteUser_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.deleteUser()).thenReturn(response)
        val requestDeleteUser = mockProfileRepository.deleteUser().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestDeleteUser)
    }

    @Test
    fun getUserPosts_with_correct_response() {
        val blogList = listOf(apiBlog)
        val response = Single.just(Response.success(blogList))
        `when`(mockApi.getUserPosts(1)).thenReturn(response)
        val requestGetUserPosts = mockProfileRepository.getUserPosts(1).blockingGet()
        assertEquals(BlogInResult.Success(listOf(domainBlog)), requestGetUserPosts)
    }

    @Test
    fun getUserPosts_with_incorrect_response() {
        val response = Single.just(Response.error<List<Blog>>(500, "".toResponseBody()))
        `when`(mockApi.getUserPosts(1)).thenReturn(response)
        val requestGetUserPosts = mockProfileRepository.getUserPosts(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetUserPosts)
    }

    @Test
    fun isUserFavorite_with_correct_response() {
        val response = Single.just(Response.success(true))
        `when`(mockApi.isFavoriteUser(1)).thenReturn(response)
        val requestIsUserFavorite = mockProfileRepository.isUserFavorite(1).blockingGet()
        assertEquals(BlogInResult.Success(true), requestIsUserFavorite)
    }

    @Test
    fun isUserFavorite_with_incorrect_response() {
        val response = Single.just(Response.error<Boolean>(500, "".toResponseBody()))
        `when`(mockApi.isFavoriteUser(1)).thenReturn(response)
        val requestIsUserFavorite = mockProfileRepository.isUserFavorite(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestIsUserFavorite)
    }

    @Test
    fun addToFavorite_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.addToFavorite(1)).thenReturn(response)
        val requestAddToFavorite = mockProfileRepository.addToFavorite(1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestAddToFavorite)
    }

    @Test
    fun addToFavorite_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.addToFavorite(1)).thenReturn(response)
        val requestAddToFavorite = mockProfileRepository.addToFavorite(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestAddToFavorite)
    }

    @Test
    fun removeFromFavorite_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.deleteFromFavorites(1)).thenReturn(response)
        val requestRemoveFromFavorite = mockProfileRepository.removeFromFavorite(1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestRemoveFromFavorite)
    }

    @Test
    fun removeFromFavorite_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.deleteFromFavorites(1)).thenReturn(response)
        val requestRemoveFromFavorite = mockProfileRepository.removeFromFavorite(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestRemoveFromFavorite)
    }
}