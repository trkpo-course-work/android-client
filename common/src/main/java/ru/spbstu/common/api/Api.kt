package ru.spbstu.common.api

import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import ru.spbstu.common.api.model.auth.*
import ru.spbstu.common.api.model.blog.Blog
import ru.spbstu.common.api.model.blog.BlogPostBody
import ru.spbstu.common.api.model.profile.PictureResponse
import ru.spbstu.common.api.model.profile.UserResponse
import ru.spbstu.common.tokens.RefreshToken

interface Api {
    @POST("/api/v1/auth/login")
    fun login(@Body loginBody: LoginBody): Single<Response<TokensResponse>>

    @POST("/api/v1/auth/logout")
    fun logout(@Body refreshToken: RefreshToken): Single<Response<Void>>

    @POST("/api/v1/auth/request_confirmation")
    fun requestConfirmation(@Body requestConfirmationBody: RequestConfirmationBody): Single<Response<Void>>

    @POST("/api/v1/auth/confirm")
    fun confirm(@Body confirmBody: ConfirmBody): Single<Response<Void>>

    @POST("/api/v1/auth/request_reset")
    fun requestReset(@Body requestResetBody: RequestResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/check_reset_code")
    fun checkResetCode(@Body checkResetBody: CheckResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/reset")
    fun reset(@Body resetBody: ResetBody): Single<Response<Void>>

    @POST("/api/v1/auth/signup")
    fun signIn(@Body signInBody: SignInBody): Single<Response<Void>>

    @PUT("/api/v1/user")
    fun editUser(@Body profile: UserResponse): Single<Response<UserResponse>>

    @GET("/api/v1/user")
    fun getUser(): Single<Response<UserResponse>>

    @GET("/api/v1/user/{id}")
    fun getUserById(@Path("id") id: Long): Single<Response<UserResponse>>

    @GET("/api/v1/user/favorites")
    fun getUserSubscriptions(): Single<Response<List<UserResponse>>>

    @POST("/api/v1/favorites/{id}")
    fun subscribeToUser(@Path("id") id: Int): Single<Response<Void>>

    @GET("/api/v1/user/news")
    fun getNews(): Single<Response<List<Blog>>>

    @GET("/api/v1/user/posts")
    fun getPosts(): Single<Response<List<Blog>>>

    @DELETE("/api/v1/user")
    fun deleteUser(): Single<Response<Void>>

    @GET("/api/v1/users")
    fun getUsers(): Single<Response<List<UserResponse>>>

    @POST("/api/v1/post")
    fun newPost(@Body blog: BlogPostBody): Single<Response<Blog>>

    @PUT("/api/v1/post/{id}")
    fun editPost(@Path("id") id: Long, @Body blog: BlogPostBody): Single<Response<Blog>>

    @DELETE("/api/v1/post/{id}")
    fun deletePost(@Path("id") id: Long): Single<Response<Void>>

    @Multipart
    @POST("/api/v1/pictures")
    fun uploadPicture(@Part file: MultipartBody.Part): Single<Response<PictureResponse>>

    @GET("/api/v1/users/{id}/posts")
    fun getUserPosts(@Path("id") id: Long): Single<Response<List<Blog>>>

    @GET("/api/v1/user/isFavorite/{id}")
    fun isFavoriteUser(@Path("id") id: Long): Single<Response<Boolean>>

    @POST("/api/v1/user/favorites/{id}")
    fun addToFavorite(@Path("id") id: Long): Single<Response<Void>>

    @DELETE("/api/v1/user/favorites/{id}")
    fun deleteFromFavorites(@Path("id") id: Long): Single<Response<Void>>
}