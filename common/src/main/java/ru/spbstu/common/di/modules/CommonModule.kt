package ru.spbstu.common.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.common.tokens.TokensRepository
import ru.spbstu.common.utils.PictureUrlHelper
import javax.inject.Named

const val SHARED_PREFERENCES_FILE = "ru.spbstu.blogin.preferences"
const val ENCRYPTED_SHARED_PREFERENCES_FILE = "ru.spbstu.blogin.encrypted"

@Module
class CommonModule {
    @Provides
    @ApplicationScope
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE)
    }

    @Provides
    @ApplicationScope
    @Named("encrypted")
    fun provideEncryptedPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_SHARED_PREFERENCES_FILE,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @ApplicationScope
    fun providePictureUrlHelper(tokensRepository: TokensRepository): PictureUrlHelper =
        PictureUrlHelper(tokensRepository)
}
