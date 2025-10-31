package ru.musindev.courseapp.di.modules

import dagger.Module
import dagger.Provides
import ru.musindev.courseapp.domain.repository.CourseRepository
import ru.musindev.courseapp.domain.usecase.GetCoursesUseCase
import ru.musindev.courseapp.domain.usecase.GetFavoriteCoursesUseCase
import ru.musindev.courseapp.domain.usecase.SortCoursesUseCase
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun provideGetCoursesUseCase(repository: CourseRepository): GetCoursesUseCase {
        return GetCoursesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: CourseRepository): ToggleFavoriteUseCase {
        return ToggleFavoriteUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteCoursesUseCase(repository: CourseRepository): GetFavoriteCoursesUseCase {
        return GetFavoriteCoursesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSortCoursesUseCase(): SortCoursesUseCase {
        return SortCoursesUseCase()
    }

}