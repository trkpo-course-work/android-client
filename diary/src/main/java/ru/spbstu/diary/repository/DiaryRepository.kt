package ru.spbstu.diary.repository

import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.blog.BlogPostBody
import ru.spbstu.common.domain.*
import java.io.File

class DiaryRepository(private val api: Api) {

    fun getPublicBlogs(): Single<BlogInResult<List<Blog>>> {
        return api.getPosts().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { it.toDomainModel() }
                    .filter { !it.isPrivate })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun getPrivateBlogs(): Single<BlogInResult<List<Blog>>> {
        return api.getPosts().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { it.toDomainModel() }.filter { it.isPrivate })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun deleteBlog(id: Long): Single<BlogInResult<Any>> {
        return api.deletePost(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun editPost(id: Long, text: String, spans: List<Span>, isPrivate: Boolean, dateTime: Long, pictureId: Long?): Single<BlogInResult<Any>> {
        return api.editPost(id, BlogPostBody(id, text, spans.map { it.toNetworkModel() }, isPrivate, dateTime, pictureId)).map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun newPost(text: String, spans: List<Span>, isPrivate: Boolean, dateTime: Long, pictureId: Long?): Single<BlogInResult<Any>> {
        return api.newPost(BlogPostBody(null, text, spans.map { it.toNetworkModel() }, isPrivate, dateTime, pictureId)).map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun uploadPicture(file: File): Single<BlogInResult<Long>> {
        val body = MultipartBody.Part.createFormData(
            "file",
            file.name,
            file.asRequestBody("image/*".toMediaTypeOrNull())
        )
        return api.uploadPicture(body).map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.id)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

}