package ru.musindev.courseapp.di.modules

import dagger.Binds
import dagger.Module
import ru.musindev.courseapp.data.repository.CourseRepositoryImpl
import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        courseRepositoryImpl: CourseRepositoryImpl
    ): CourseRepository
}
