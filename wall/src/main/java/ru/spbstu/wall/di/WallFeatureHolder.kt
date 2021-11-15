package ru.spbstu.wall.di

import ru.spbstu.common.di.FeatureApiHolder
import ru.spbstu.common.di.FeatureContainer
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.wall.WallRouter
import javax.inject.Inject

@ApplicationScope
class WallFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val featureRouter: WallRouter
) : FeatureApiHolder(featureContainer) {
    override fun initializeDependencies(): Any {
        val deps = DaggerWallComponent_WallDependenciesComponent.builder()
            .commonApi(commonApi())
            .build()
        return DaggerWallComponent.factory()
            .create(featureRouter, deps)
    }
}