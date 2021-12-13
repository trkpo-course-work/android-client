package ru.spbstu.search.repository

import io.reactivex.rxjava3.core.Single
import ru.spbstu.common.api.Api
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.search.search.domain.SearchResult

class SearchRepository(private val api: Api) {
    fun getSearchResults(): Single<BlogInResult<List<SearchResult>>> {
        return api.getUsers().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { SearchResult(it.id, it.name) })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }
}