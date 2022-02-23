package ru.spbstu.profile.repository

import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.spbstu.common.api.Api
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.api.model.toDomainModel
import ru.spbstu.common.domain.*
import ru.spbstu.common.tokens.TokensRepository
import java.io.File

class ProfileRepository(private val api: Api, private val tokensRepository: TokensRepository) {
    fun getUser(): Single<BlogInResult<UserProfile>> {
        return api.getUser().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.toDomainModel())
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun logout(): Single<BlogInResult<Any>> {
        return api.logout(tokensRepository.getRefresh()!!).map {
            if (it.isSuccessful) {
                tokensRepository.clearTokens()
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun getUserSubscriptions(): Single<BlogInResult<List<UserProfile>>> {
        return api.getUserSubscriptions().map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { it.toDomainModel() })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun editProfile(newProfile: UserProfile): Single<BlogInResult<UserProfile>> {
        return api.editUser(
            UserResponse(
                newProfile.id,
                newProfile.name,
                newProfile.pictureId,
                newProfile.login,
                newProfile.email,
                newProfile.password,
                newProfile.newPassword
            )
        ).map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.toDomainModel())
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun getUserInfo(id: Long): Single<BlogInResult<UserProfile>> {
        return api.getUserById(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.toDomainModel())
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

    fun deleteUser(): Single<BlogInResult<Any>> {
        return api.deleteUser().map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun getUserPosts(id: Long): Single<BlogInResult<List<Blog>>> {
        return api.getUserPosts(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!.map { it.toDomainModel() })
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun isUserFavorite(id: Long): Single<BlogInResult<Boolean>> {
        return api.isFavoriteUser(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(it.body()!!)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun addToFavorite(id: Long): Single<BlogInResult<Any>> {
        return api.addToFavorite(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }

    fun removeFromFavorite(id: Long): Single<BlogInResult<Any>> {
        return api.deleteFromFavorites(id).map {
            if (it.isSuccessful) {
                BlogInResult.Success(EMPTY_RESULT)
            } else {
                BlogInResult.Error(UNKNOWN_ERROR)
            }
        }
    }
}
