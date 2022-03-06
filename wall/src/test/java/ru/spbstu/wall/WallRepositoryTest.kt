package ru.spbstu.wall

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
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.common.domain.UserProfile
import ru.spbstu.wall.repository.WallRepository

class WallRepositoryTest {
    private lateinit var mockApi: Api
    private lateinit var mockWallRepository: WallRepository

    private val userResponse1: UserResponse = UserResponse(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val userResponse2: UserResponse = UserResponse(2, "name2", 2,
        "login2", "email2", "pass2", "newPass2")
    private val userProfile1: UserProfile = UserProfile(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val userProfile2: UserProfile = UserProfile(2, "name2", 2,
        "login2", "email2", "pass2", "newPass2")
    private val apiBlog1: Blog = Blog(1, userResponse1, "text", null, false, 123456, 1)
    private val apiBlog2: Blog = Blog(2, userResponse2, "text2", null, false, 123456, 2)
    private val domainBlog1: ru.spbstu.common.domain.Blog =
        ru.spbstu.common.domain.Blog(1, userProfile1, "text", null, false, 123456, 1)
    private val domainBlog2: ru.spbstu.common.domain.Blog =
        ru.spbstu.common.domain.Blog(2, userProfile2, "text2", null, false, 123456, 2)
    private val listApiBlogs = listOf(apiBlog1, apiBlog2)
    private val listDomainBlogs = listOf(domainBlog1, domainBlog2)

    @Before
    fun setUp() {
        mockApi = mock(Api::class.java)
        mockWallRepository = WallRepository(mockApi)
    }

    @Test
    fun getNews_with_correct_response() {
        val response = Single.just(Response.success(listApiBlogs))
        `when`(mockApi.getNews()).thenReturn(response)
        val requestGetNews = mockWallRepository.getNews().blockingGet()
        assertEquals(BlogInResult.Success(listDomainBlogs), requestGetNews)
    }

    @Test
    fun getNews_with_incorrect_response() {
        val response = Single.just(Response.error<List<Blog>>(500, "".toResponseBody()))
        `when`(mockApi.getNews()).thenReturn(response)
        val requestGetNews = mockWallRepository.getNews().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetNews)
    }
}