package ru.spbstu.diary

import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.SpanType
import ru.spbstu.common.api.model.blog.Blog
import ru.spbstu.common.api.model.Span
import ru.spbstu.common.api.model.blog.BlogPostBody
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.domain.*
import ru.spbstu.diary.repo.DiaryRepository

class DiaryRepositoryTest {
    private lateinit var mockApi: Api
    private lateinit var mockDiaryRepository: DiaryRepository

    private val userResponse: UserResponse = UserResponse(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val userProfile: UserProfile = UserProfile(1, "name", 1,
        "login", "email", "pass", "newPass")
    private val publicApiBlog: Blog = Blog(1, userResponse, "text", null, false, 123456, 1)
    private val publicDomainBlog: ru.spbstu.common.domain.Blog = Blog(1, userProfile, "text", null, false, 123456, 1)
    private val privateApiBlog: Blog = Blog(1, userResponse, "text", null, true, 123456, 1)
    private val privateDomainBlog: ru.spbstu.common.domain.Blog = Blog(1, userProfile, "text", null, true, 123456, 1)
    private val blogApiList = listOf(publicApiBlog, privateApiBlog)

    @Before
    fun setUp() {
        mockApi = mock(Api::class.java)
        mockDiaryRepository = DiaryRepository(mockApi)
    }

    @Test
    fun getPublicBlogs_with_correct_response() {
        val response = Single.just(Response.success(blogApiList))
        `when`(mockApi.getPosts()).thenReturn(response)
        val requestGetPublicBlogs = mockDiaryRepository.getPublicBlogs().blockingGet()
        assertEquals(BlogInResult.Success(listOf(publicDomainBlog)), requestGetPublicBlogs)
    }

    @Test
    fun getPublicBlogs_with_incorrect_response() {
        val response = Single.just(Response.error<List<Blog>>(500, "".toResponseBody()))
        `when`(mockApi.getPosts()).thenReturn(response)
        val requestGetPublicBlogs = mockDiaryRepository.getPublicBlogs().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetPublicBlogs)
    }

    @Test
    fun getPrivateBlogs_with_correct_response() {
        val response = Single.just(Response.success(blogApiList))
        `when`(mockApi.getPosts()).thenReturn(response)
        val requestGetPrivateBlogs = mockDiaryRepository.getPrivateBlogs().blockingGet()
        assertEquals(BlogInResult.Success(listOf(privateDomainBlog)), requestGetPrivateBlogs)
    }

    @Test
    fun getPrivateBlogs_with_incorrect_response() {
        val response = Single.just(Response.error<List<Blog>>(500, "".toResponseBody()))
        `when`(mockApi.getPosts()).thenReturn(response)
        val requestGetPrivateBlogs = mockDiaryRepository.getPrivateBlogs().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetPrivateBlogs)
    }

    @Test
    fun deleteBlog_with_correct_response() {
        val response = Single.just(Response.success<Void?>(null))
        `when`(mockApi.deletePost(1)).thenReturn(response)
        val requestDeleteBlog = mockDiaryRepository.deleteBlog(1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestDeleteBlog)
    }

    @Test
    fun deleteBlog_with_incorrect_response() {
        val response = Single.just(Response.error<Void?>(500, "".toResponseBody()))
        `when`(mockApi.deletePost(1)).thenReturn(response)
        val requestDeleteBlog = mockDiaryRepository.deleteBlog(1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestDeleteBlog)
    }

    @Test
    fun editPost_with_correct_response() {
        val response = Single.just(Response.success(publicApiBlog))
        val spanApiList = listOf(Span("BOLD", 1, 2))
        val spanDomainList = listOf(Span(SpanType.BOLD, 1, 2))
        val blogPostBody = BlogPostBody(1, "text", spanApiList, false, 123456, 1)
        `when`(mockApi.editPost(1, blogPostBody)).thenReturn(response)
        val requestEditPost = mockDiaryRepository.editPost(1, "text", spanDomainList, false, 123456, 1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestEditPost)
    }

    @Test
    fun editPost_with_incorrect_response() {
        val response = Single.just(Response.error<Blog>(500, "".toResponseBody()))
        val spanApiList = listOf(Span("BOLD", 1, 2))
        val spanDomainList = listOf(Span(SpanType.BOLD, 1, 2))
        val blogPostBody = BlogPostBody(1, "text", spanApiList, false, 123456, 1)
        `when`(mockApi.editPost(1, blogPostBody)).thenReturn(response)
        val requestEditBlog = mockDiaryRepository.editPost(1, "text", spanDomainList, false, 123456, 1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestEditBlog)
    }

    @Test
    fun newPost_with_correct_response() {
        val response = Single.just(Response.success(publicApiBlog))
        val spanApiList = listOf(Span("BOLD", 1, 2))
        val spanDomainList = listOf(Span(SpanType.BOLD, 1, 2))
        val blogPostBody = BlogPostBody(null, "text", spanApiList, false, 123456, 1)
        `when`(mockApi.newPost(blogPostBody)).thenReturn(response)
        val requestNewPost = mockDiaryRepository.newPost("text", spanDomainList, false, 123456, 1).blockingGet()
        assertEquals(BlogInResult.Success(EMPTY_RESULT), requestNewPost)
    }

    @Test
    fun newPost_with_incorrect_response() {
        val response = Single.just(Response.error<Blog>(500, "".toResponseBody()))
        val spanApiList = listOf(Span("BOLD", 1, 2))
        val spanDomainList = listOf(Span(SpanType.BOLD, 1, 2))
        val blogPostBody = BlogPostBody(null, "text", spanApiList, false, 123456, 1)
        `when`(mockApi.newPost(blogPostBody)).thenReturn(response)
        val requestNewPost = mockDiaryRepository.newPost("text", spanDomainList, false, 123456, 1).blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestNewPost)
    }

//    @Test
//    fun uploadPicture_with_correct_response() {
//        val pictureResponse = PictureResponse(1)
//        val file = File("test.txt")
//        file.writeText("text")
//        val body = MultipartBody.Part.createFormData(
//            "file",
//            file.name,
//            file.asRequestBody("image/*".toMediaTypeOrNull())
//        )
//        val response = Single.just(Response.success(pictureResponse))
//        `when`(mockApi.uploadPicture(body)).thenReturn(response)
//        val requestUploadPicture = mockDiaryRepository.uploadPicture(file).blockingGet()
//        assertEquals(BlogInResult.Success(1), requestUploadPicture)
//    }
//
//    @Test
//    fun uploadPicture_with_incorrect_response() {
//        val file = File("test.jpg")
//        val body = MultipartBody.Part.createFormData(
//            "file",
//            file.name,
//            file.asRequestBody("image/*".toMediaTypeOrNull())
//        )
//        val response = Single.just(Response.error<PictureResponse>(500, "".toResponseBody()))
//        `when`(mockApi.uploadPicture(body)).thenReturn(response)
//        val requestUploadPicture = mockDiaryRepository.uploadPicture(file).blockingGet()
//        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestUploadPicture)
//    }
}