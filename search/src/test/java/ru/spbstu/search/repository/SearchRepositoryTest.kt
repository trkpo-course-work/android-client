package ru.spbstu.search.repository

import ru.spbstu.search.search.domain.SearchResult
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.mockito.Mockito.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.UNKNOWN_ERROR

class SearchRepositoryTest {
    private lateinit var mockApi: Api
    private lateinit var mockSearchRepository: SearchRepository

    private val userResponseApi1: UserResponse = UserResponse(1, "name", null, "login", "email", "pass", null)
    private val searchResult1: SearchResult = SearchResult(userResponseApi1.id, userResponseApi1.name, userResponseApi1.login)
    private val userResponseApi2: UserResponse = UserResponse(2, "name2", null, "login2", "email2", "pass2", null)
    private val searchResult2: SearchResult = SearchResult(userResponseApi2.id, userResponseApi2.name, userResponseApi2.login)
    private val userList = listOf(userResponseApi1, userResponseApi2)
    private val searchResultList = listOf(searchResult1, searchResult2)

    @Before
    fun setUp() {
        mockApi = mock(Api::class.java)
        mockSearchRepository = SearchRepository(mockApi)
    }

    @Test
    fun getSearchResults_with_correct_response() {
        val response = Single.just(Response.success(userList))
        `when`(mockApi.getUsers()).thenReturn(response)
        val requestGetSearchResults = mockSearchRepository.getSearchResults().blockingGet()
        assertEquals(BlogInResult.Success(searchResultList), requestGetSearchResults)
    }

    @Test
    fun getSearchResults_with_incorrect_response() {
        val response = Single.just(Response.error<List<UserResponse>>(500, "".toResponseBody()))
        `when`(mockApi.getUsers()).thenReturn(response)
        val requestGetSearchResults = mockSearchRepository.getSearchResults().blockingGet()
        assertEquals(BlogInResult.Error<Any>(UNKNOWN_ERROR), requestGetSearchResults)
    }
}