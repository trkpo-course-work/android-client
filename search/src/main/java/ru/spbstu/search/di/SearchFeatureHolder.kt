package ru.spbstu.search.di

import ru.spbstu.common.di.FeatureApiHolder
import ru.spbstu.common.di.FeatureContainer
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.search.SearchRouter
import javax.inject.Inject

@ApplicationScope
class SearchFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val featureRouter: SearchRouter
) : FeatureApiHolder(featureContainer) {
    override fun initializeDependencies(): Any {
        val deps = DaggerSearchComponent_SearchDependenciesComponent.builder()
            .commonApi(commonApi())
            .build()
        return DaggerSearchComponent.factory()
            .create(featureRouter, deps)
    }
}