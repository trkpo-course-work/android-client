package ru.spbstu.blog

import android.app.Application
import ru.spbstu.blog.di.app.AppComponent
import ru.spbstu.blog.di.app.DaggerAppComponent
import ru.spbstu.blog.di.deps.FeatureHolderManager
import ru.spbstu.blog.log.ReleaseTree
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.FeatureContainer
import timber.log.Timber
import javax.inject.Inject

class App: Application(), FeatureContainer {


    @Inject
    lateinit var featureHolderManager: FeatureHolderManager

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        appComponent.inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    override fun <T> getFeature(key: Class<*>): T {
        return featureHolderManager.getFeature<T>(key)!!
    }

    override fun releaseFeature(key: Class<*>) {
        featureHolderManager.releaseFeature(key)
    }

    override fun commonApi(): CommonApi {
        return appComponent
    }


}