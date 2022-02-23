package ru.spbstu.search.repository

import io.reactivex.rxjava3.core.Single
import ru.spbstu.common.api.Api
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.EMPTY_RESULT
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.search.search.domain.SearchResult

class SearchRepository(private val api: Api) {
    fun getSearchResults(): Single<BlogInResult<List<SearchResult>>> {
        return api.getUsers().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map {
                    val isFav = api.isFavoriteUser(it.id).blockingGet().body() ?: false
                    SearchResult(it.id, it.name, it.login, it.pictureId, isFav)
                })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun setFavorite(id: Long, isFav: Boolean): Single<BlogInResult<Any>> {
        return if (isFav) {
            api.addToFavorite(id).map {
                if (it.isSuccessful) {
                    BlogInResult.Success(EMPTY_RESULT)
                } else {
                    BlogInResult.Error(UNKNOWN_ERROR)
                }
            }
        } else {
            api.deleteFromFavorites(id).map {
                if (it.isSuccessful) {
                    BlogInResult.Success(EMPTY_RESULT)
                } else {
                    BlogInResult.Error(UNKNOWN_ERROR)
                }
            }
        }
    }
}