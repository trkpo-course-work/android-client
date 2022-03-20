package ru.spbstu.blog

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import retrofit2.Response
import ru.spbstu.auth.domain.Tokens
import ru.spbstu.auth.repository.AuthRepository
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.auth.*
import ru.spbstu.common.api.model.blog.BlogPostBody
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.domain.*
import ru.spbstu.common.tokens.TokensRepository
import ru.spbstu.diary.repo.DiaryRepository
import ru.spbstu.profile.repository.ProfileRepository
import ru.spbstu.search.repository.SearchRepository
import ru.spbstu.search.search.domain.SearchResult
import ru.spbstu.wall.repository.WallRepository


class IntegrationTests {
    private lateinit var mockApi: Api
    private lateinit var mockTokenRepository: TokensRepository
    private lateinit var successfulTokensResponse: TokensResponse
    private lateinit var authRepository: AuthRepository
    private lateinit var profileRepository: ProfileRepository
    private lateinit var searchRepository: SearchRepository
    private lateinit var diaryRepository: DiaryRepository
    private lateinit var wallRepository: WallRepository

    private val correctLoginBody: LoginBody = LoginBody("correct", "correct")
    private val incorrectLoginBody: LoginBody = LoginBody("incorrect", "incorrect")
    private val emptyLoginBody: LoginBody = LoginBody("", "")
    private val accessToken = "access token"
    private val refreshToken = "refresh token"

    @Before
    fun setUp() {
        mockApi = Mockito.mock(Api::class.java)
        mockTokenRepository = Mockito.mock(TokensRepository::class.java)
        successfulTokensResponse = TokensResponse(accessToken, refreshToken)
        authRepository = AuthRepository(mockApi, mockTokenRepository)
        profileRepository = ProfileRepository(mockApi, mockTokenRepository)
        searchRepository = SearchRepository(mockApi)
        diaryRepository = DiaryRepository(mockApi)
        wallRepository = WallRepository(mockApi)
    }

    @Test
    fun authFlowTest() {
        val response = Single.just(Response.success(successfulTokensResponse))
        Mockito.`when`(mockApi.login(correctLoginBody)).thenReturn(response)
        val signInResponse = Single.just(Response.success<Void?>(null))
        val correctSignInBody =
            SignInBody("any", correctLoginBody.login, "anyEmail", correctLoginBody.password)
        Mockito.`when`(mockApi.signIn(correctSignInBody)).thenReturn(signInResponse)
        val newsResponse =
            Single.just(Response.success<List<ru.spbstu.common.api.model.blog.Blog>>(emptyList()))
        Mockito.`when`(mockApi.getNews()).thenReturn(newsResponse)

        val signInResult = authRepository.signIn(
            "any",
            correctSignInBody.login,
            "anyEmail",
            correctSignInBody.password
        ).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), signInResult)

        val loginResponse = authRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Success(Tokens(accessToken, refreshToken)), loginResponse)

        val newsResult = wallRepository.getNews().blockingGet()
        assertEquals(BlogInResult.Success(emptyList<Blog>()), newsResult)

        Mockito.verify(mockTokenRepository).saveRefresh(refreshToken)
        Mockito.verify(mockTokenRepository).saveToken(accessToken)
    }

    @Test
    fun editProfileAndGetNewDataTest() {
        val oldUser = UserProfile(1, "name", 1, "login", "email", null, null)
        val oldUserApi =
            UserResponse(oldUser.id, oldUser.name, 1, oldUser.login, oldUser.email, null, null)
        val newUserApi =
            UserResponse(oldUser.id, oldUser.name, null, oldUser.login, oldUser.email, null, null)
        val newUserDomain =
            UserProfile(oldUser.id, oldUser.name, null, oldUser.login, oldUser.email, null, null)
        val response = Single.just(Response.success(newUserApi))
        val getUserResponse = Single.just(Response.success(newUserApi))
        val getUserResponseOld = Single.just(Response.success(oldUserApi))
        Mockito.`when`(mockApi.getUser()).thenReturn(getUserResponseOld)
        val requestGetUserOld = profileRepository.getUser().blockingGet()
        assertEquals(BlogInResult.Success(oldUser), requestGetUserOld)
        Mockito.`when`(mockApi.editUser(newUserApi)).thenReturn(response)
        Mockito.`when`(mockApi.getUser()).thenReturn(getUserResponse)
        val requestEditProfile = profileRepository.editProfile(newUserDomain).blockingGet()
        assertEquals(BlogInResult.Success(newUserDomain), requestEditProfile)
        val requestGetUser = profileRepository.getUser().blockingGet()
        assertEquals(BlogInResult.Success(newUserDomain), requestGetUser)
    }

    @Test
    fun searchUserAndGetHisInfo() {
        val userResponseApi1 = UserResponse(1, "name1", 1, "login1", "email1", null, null)
        val searchResult1 = SearchResult(
            userResponseApi1.id,
            userResponseApi1.name,
            userResponseApi1.login,
            1,
            false
        )
        val userResponseApi2 = UserResponse(2, "name2", 2, "login2", "email2", null, null)
        val searchResult2 = SearchResult(
            userResponseApi2.id,
            userResponseApi2.name,
            userResponseApi2.login,
            2,
            false
        )
        val userList = listOf(userResponseApi1, userResponseApi2)
        val searchResultList = listOf(searchResult1, searchResult2)
        val searchResponse = Single.just(Response.success(userList))
        Mockito.`when`(mockApi.getUsers()).thenReturn(searchResponse)
        Mockito.`when`(mockApi.isFavoriteUser(1)).thenReturn(Single.just(Response.success(false)))
        Mockito.`when`(mockApi.isFavoriteUser(2)).thenReturn(Single.just(Response.success(false)))

        val userBlogs = listOf(
            ru.spbstu.common.api.model.blog.Blog(
                1,
                userResponseApi1,
                "blog1",
                null,
                false,
                1234L,
                null
            ),
            ru.spbstu.common.api.model.blog.Blog(
                2,
                userResponseApi1,
                "blog2",
                null,
                false,
                1254L,
                null
            ),
        )
        Mockito.`when`(mockApi.getUserPosts(1)).thenReturn(Single.just(Response.success(userBlogs)))
        val userBlogsResult = BlogInResult.Success(userBlogs.map { it.toDomainModel() })

        val addToFavoritesResponse = Single.just(Response.success<Void?>(null))
        Mockito.`when`(mockApi.addToFavorite(1)).thenReturn(addToFavoritesResponse)

        val requestGetSearchResults = searchRepository.getSearchResults().blockingGet()
        assertEquals(BlogInResult.Success(searchResultList), requestGetSearchResults)

        val requestUserBlogs = profileRepository.getUserPosts(1).blockingGet()
        assertEquals(userBlogsResult, requestUserBlogs)

        val requestAddToFavorite = profileRepository.addToFavorite(1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestAddToFavorite)
    }

    @Test
    fun testLoginAndNewBlog() {
        val response = Single.just(Response.success(successfulTokensResponse))
        Mockito.`when`(mockApi.login(correctLoginBody)).thenReturn(response)

        val loginResponse = authRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Success(Tokens(accessToken, refreshToken)), loginResponse)
        Mockito.verify(mockTokenRepository).saveRefresh(refreshToken)
        Mockito.verify(mockTokenRepository).saveToken(accessToken)

        val blogBody = BlogPostBody(null, "blog1", emptyList(), false, 1234L, 5)
        val currentUserApi = UserResponse(1, "name1", 9, "login1", "email1", null, null)
        val blogData = ru.spbstu.common.api.model.blog.Blog(1, currentUserApi, "blog1", null, false, 1234L, 5)
        val blogResponse = Single.just(Response.success(blogData))
        Mockito.`when`(mockApi.newPost(blogBody)).thenReturn(blogResponse)

        val createPostResult = diaryRepository.newPost("blog1", emptyList(), false, 1234L, 5).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), createPostResult)

        val userBlogs = listOf(
            blogData
        )
        Mockito.`when`(mockApi.getUserPosts(1)).thenReturn(Single.just(Response.success(userBlogs)))

        val getUserBlogsResult = profileRepository.getUserPosts(1).blockingGet()
        assertEquals(BlogInResult.Success(listOf(blogData.toDomainModel())), getUserBlogsResult)
    }

    @Test
    fun testLoginAndPrivateBlog() {
        val response = Single.just(Response.success(successfulTokensResponse))
        Mockito.`when`(mockApi.login(correctLoginBody)).thenReturn(response)

        val loginResponse = authRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Success(Tokens(accessToken, refreshToken)), loginResponse)
        Mockito.verify(mockTokenRepository).saveRefresh(refreshToken)
        Mockito.verify(mockTokenRepository).saveToken(accessToken)

        val blogBody = BlogPostBody(null, "blog1", emptyList(), true, 1234L, 5)
        val currentUserApi = UserResponse(1, "name1", 9, "login1", "email1", null, null)
        val blogData = ru.spbstu.common.api.model.blog.Blog(1, currentUserApi, "blog1", null, true, 1234L, 5)
        val blogResponse = Single.just(Response.success(blogData))
        Mockito.`when`(mockApi.newPost(blogBody)).thenReturn(blogResponse)

        val createPostResult = diaryRepository.newPost("blog1", emptyList(), true, 1234L, 5).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), createPostResult)

        val anyBlogs = listOf(
            ru.spbstu.common.api.model.blog.Blog(2, currentUserApi, "blog1", null, false, 1254L, 5),
            ru.spbstu.common.api.model.blog.Blog(3, currentUserApi, "blog1", null, false, 1634L, 9),
            ru.spbstu.common.api.model.blog.Blog(4, currentUserApi, "blog1", null, false, 1934L, 11),
        )
        Mockito.`when`(mockApi.getNews()).thenReturn(Single.just(Response.success(anyBlogs)))

        val getUserBlogsResult = wallRepository.getNews().blockingGet()
        assertEquals(BlogInResult.Success(anyBlogs.map { it.toDomainModel() }), getUserBlogsResult)
        val data = (getUserBlogsResult as BlogInResult.Success).data
        assert(data.none { it.isPrivate })
        assert(data.none { it == blogData.toDomainModel() })
    }

    @Test
    fun searchUserAndAddToFavorite() {
        val userResponseApi1 = UserResponse(1, "name1", 1, "login1", "email1", null, null)
        val searchResult1 = SearchResult(
            userResponseApi1.id,
            userResponseApi1.name,
            userResponseApi1.login,
            1,
            false
        )
        val userResponseApi2 = UserResponse(2, "name2", 2, "login2", "email2", null, null)
        val searchResult2 = SearchResult(
            userResponseApi2.id,
            userResponseApi2.name,
            userResponseApi2.login,
            2,
            false
        )
        val userList = listOf(userResponseApi1, userResponseApi2)
        val searchResultList = listOf(searchResult1, searchResult2)
        val searchResponse = Single.just(Response.success(userList))
        Mockito.`when`(mockApi.getUsers()).thenReturn(searchResponse)
        Mockito.`when`(mockApi.isFavoriteUser(1)).thenReturn(Single.just(Response.success(false)))
        Mockito.`when`(mockApi.isFavoriteUser(2)).thenReturn(Single.just(Response.success(false)))

        val userBlogs = listOf(
            ru.spbstu.common.api.model.blog.Blog(
                1,
                userResponseApi1,
                "blog1",
                null,
                false,
                1234L,
                null
            ),
            ru.spbstu.common.api.model.blog.Blog(
                2,
                userResponseApi1,
                "blog2",
                null,
                false,
                1254L,
                null
            ),
            ru.spbstu.common.api.model.blog.Blog(
                3,
                userResponseApi2,
                "blog3",
                null,
                false,
                12564L,
                null
            ),
        )
        Mockito.`when`(mockApi.getNews()).thenReturn(Single.just(Response.success(userBlogs)))

        val addToFavoritesResponse = Single.just(Response.success<Void?>(null))
        Mockito.`when`(mockApi.addToFavorite(1)).thenReturn(addToFavoritesResponse)

        val requestGetSearchResults = searchRepository.getSearchResults().blockingGet()
        assertEquals(BlogInResult.Success(searchResultList), requestGetSearchResults)

        val newsExpected = BlogInResult.Success(userBlogs.map { it.toDomainModel() })
        val requestNews = wallRepository.getNews().blockingGet()
        assertEquals(newsExpected, requestNews)
        assert(newsExpected.data.filter { it.user.id == 1L }.size == 2)

        val requestAddToFavorite = profileRepository.addToFavorite(1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestAddToFavorite)
    }

    @Test
    fun loginAndEditPublicBlog() {
        val response = Single.just(Response.success(successfulTokensResponse))
        Mockito.`when`(mockApi.login(correctLoginBody)).thenReturn(response)

        val loginResponse = authRepository.login("correct", "correct").blockingGet()
        assertEquals(BlogInResult.Success(Tokens(accessToken, refreshToken)), loginResponse)
        Mockito.verify(mockTokenRepository).saveRefresh(refreshToken)
        Mockito.verify(mockTokenRepository).saveToken(accessToken)

        val blogBody = BlogPostBody(1, "blog1", emptyList(), false, 1234L, 5)
        val currentUserApi = UserResponse(1, "name1", 9, "login1", "email1", null, null)
        val blogData = ru.spbstu.common.api.model.blog.Blog(1, currentUserApi, "blog1", null, false, 1234L, 5)
        val blogResponse = Single.just(Response.success(blogData))
        Mockito.`when`(mockApi.editPost(1, blogBody)).thenReturn(blogResponse)

        val createPostResult = diaryRepository.editPost(1, "blog1", emptyList(), false, 1234L, 5).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), createPostResult)

        val userBlogs = listOf(
            blogData
        )
        Mockito.`when`(mockApi.getUserPosts(1)).thenReturn(Single.just(Response.success(userBlogs)))

        val getUserBlogsResult = profileRepository.getUserPosts(1).blockingGet()
        assertEquals(BlogInResult.Success(listOf(blogData.toDomainModel())), getUserBlogsResult)
    }

    @Test
    fun editPrivatePost() {
        val blogBody = BlogPostBody(1, "blog1", emptyList(), true, 1234L, 5)
        val currentUserApi = UserResponse(1, "name1", 9, "login1", "email1", null, null)
        val blogData = ru.spbstu.common.api.model.blog.Blog(1, currentUserApi, "blog1", null, true, 1234L, 5)
        val blogResponse = Single.just(Response.success(blogData))
        Mockito.`when`(mockApi.editPost(1, blogBody)).thenReturn(blogResponse)

        val createPostResult = diaryRepository.editPost(1, "blog1", emptyList(), true, 1234L, 5).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), createPostResult)

        val anyBlogs = listOf(
            ru.spbstu.common.api.model.blog.Blog(2, currentUserApi, "blog1", null, false, 1254L, 5),
            ru.spbstu.common.api.model.blog.Blog(3, currentUserApi, "blog1", null, false, 1634L, 9),
            ru.spbstu.common.api.model.blog.Blog(4, currentUserApi, "blog1", null, false, 1934L, 11),
        )
        Mockito.`when`(mockApi.getNews()).thenReturn(Single.just(Response.success(anyBlogs)))

        val getUserBlogsResult = wallRepository.getNews().blockingGet()
        assertEquals(BlogInResult.Success(anyBlogs.map { it.toDomainModel() }), getUserBlogsResult)
        val data = (getUserBlogsResult as BlogInResult.Success).data
        assert(data.none { it.isPrivate })
        assert(data.none { it == blogData.toDomainModel() })
    }

    @Test
    fun authWithInvalidData() {
        val response = Single.just(Response.error<TokensResponse>(500, "".toResponseBody()))
        Mockito.`when`(mockApi.login(incorrectLoginBody)).thenReturn(response)
        val signInResponse = Single.just(Response.success<Void?>(null))
        val correctSignInBody =
            SignInBody("any", correctLoginBody.login, "anyEmail", correctLoginBody.password)
        Mockito.`when`(mockApi.signIn(correctSignInBody)).thenReturn(signInResponse)

        val signInResult = authRepository.signIn(
            "any",
            correctSignInBody.login,
            "anyEmail",
            correctSignInBody.password
        ).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), signInResult)

        val loginResponse = authRepository.login("incorrect", "incorrect").blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), loginResponse)
    }

    @Test
    fun authWithEmptyData() {
        val response = Single.just(Response.error<TokensResponse>(500, "".toResponseBody()))
        Mockito.`when`(mockApi.login(emptyLoginBody)).thenReturn(response)
        val signInResponse = Single.just(Response.success<Void?>(null))
        val correctSignInBody =
            SignInBody("any", correctLoginBody.login, "anyEmail", correctLoginBody.password)
        Mockito.`when`(mockApi.signIn(correctSignInBody)).thenReturn(signInResponse)

        val signInResult = authRepository.signIn(
            "any",
            correctSignInBody.login,
            "anyEmail",
            correctSignInBody.password
        ).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), signInResult)

        val loginResponse = authRepository.login("", "").blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), loginResponse)
    }

    @Test
    fun signInWithIncorrectCode() {
        val signInResponse = Single.just(Response.success<Void?>(null))
        val correctSignInBody =
            SignInBody("any", correctLoginBody.login, "anyEmail", correctLoginBody.password)
        Mockito.`when`(mockApi.signIn(correctSignInBody)).thenReturn(signInResponse)

        val signInResult = authRepository.signIn(
            "any",
            correctSignInBody.login,
            "anyEmail",
            correctSignInBody.password
        ).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), signInResult)

        val requestConfirmationBody = RequestConfirmationBody(correctLoginBody.login)
        val responseConfirmationBody = Single.just(Response.success<Void?>(null))
        Mockito.`when`(mockApi.requestConfirmation(requestConfirmationBody)).thenReturn(responseConfirmationBody)

        val sendCodeResult = authRepository.requestConfirm(correctLoginBody.login).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), sendCodeResult)

        val confirmBody = ConfirmBody(correctLoginBody.login, "incorrect")
        val responseConfirmBody = Single.just(Response.error<Void>(500, "".toResponseBody()))
        Mockito.`when`(mockApi.confirm(confirmBody)).thenReturn(responseConfirmBody)

        val confirmResult = authRepository.confirm(confirmBody.login, confirmBody.code).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), confirmResult)
    }

}