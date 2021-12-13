package ru.spbstu.wall.repository

import io.reactivex.rxjava3.core.Single
import ru.spbstu.common.api.Api
import ru.spbstu.common.domain.Blog
import ru.spbstu.common.domain.BlogInResult
import ru.spbstu.common.domain.UNKNOWN_ERROR
import ru.spbstu.common.domain.toDomainModel

class WallRepository(private val api: Api) {
    fun getNews(): Single<BlogInResult<List<Blog>>> {
        return api.getNews().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { it.toDomainModel() })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }
}