package ru.spbstu.diary.di

import ru.spbstu.common.di.FeatureApiHolder
import ru.spbstu.common.di.FeatureContainer
import ru.spbstu.common.di.scope.ApplicationScope
import ru.spbstu.diary.DiaryRouter
import javax.inject.Inject

@ApplicationScope
class DiaryFeatureHolder @Inject constructor(
    featureContainer: FeatureContainer,
    private val featureRouter: DiaryRouter
) : FeatureApiHolder(featureContainer) {
    override fun initializeDependencies(): Any {
        val deps = DaggerDiaryComponent_DiaryDependenciesComponent.builder()
            .commonApi(commonApi())
            .build()
        return DaggerDiaryComponent.factory()
            .create(featureRouter, deps)
    }
}