package ru.spbstu.diary.di

import dagger.BindsInstance
import dagger.Component
import ru.spbstu.common.di.CommonApi
import ru.spbstu.common.di.scope.FeatureScope
import ru.spbstu.diary.DiaryRouter
import ru.spbstu.diary.diary.di.DiaryDiaryComponent
import ru.spbstu.diary.post.di.PostComponent
import ru.spbstu.diary.user_blog.di.UserBlogComponent
import ru.spbstu.diary.user_diary.di.UserDiaryComponent

@Component(
    dependencies = [
        DiaryDependencies::class,
    ],
    modules = [
        DiaryModule::class,
        DiaryDataModule::class
    ]
)
@FeatureScope
interface DiaryComponent {

    fun diaryDiaryComponentFactory(): DiaryDiaryComponent.Factory
    fun userDiaryComponentFactory(): UserDiaryComponent.Factory
    fun userBlogComponentFactory(): UserBlogComponent.Factory
    fun postComponentFactory(): PostComponent.Factory

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance featureRouter: DiaryRouter,
            deps: DiaryDependencies
        ): DiaryComponent
    }

    @Component(
        dependencies = [
            CommonApi::class,
        ]
    )
    interface DiaryDependenciesComponent : DiaryDependencies
}