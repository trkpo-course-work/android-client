package ru.spbstu.profile.di

import ru.spbstu.common.di.FeatureApiHolder
import ru.spbstu.common.di.FeatureContainer
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.profile.ProfileRouter
import javax.inject.Inject

@ApplicationScope
class ProfileFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val featureRouter: ProfileRouter
) : FeatureApiHolder(featureContainer) {
    override fun initializeDependencies(): Any {
        val deps = DaggerProfileComponent_ProfileDependenciesComponent.builder()
            .commonApi(commonApi())
            .build()
        return DaggerProfileComponent.factory()
            .create(featureRouter, deps)
    }
}