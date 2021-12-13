package ru.spbstu.common.utils

import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import ru.spbstu.common.BuildConfig
import ru.spbstu.common.tokens.TokensRepository

public class PictureUrlHelper(private val tokensRepository: TokensRepository) {
    public fun getPictureUrl(pictureId: Long): GlideUrl {
        return GlideUrl(
            "${BuildConfig.ENDPOINT}api/v1/pictures/$pictureId", LazyHeaders.Builder()
                .addHeader("AUTHORIZATION", "Bearer ${tokensRepository.getToken()}")
                .build()
        )
    }
}