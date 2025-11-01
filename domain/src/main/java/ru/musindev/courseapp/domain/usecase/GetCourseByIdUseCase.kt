package ru.musindev.courseapp.domain.usecase

import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Inject

class GetCourseByIdUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: Int): Result<Course> {
        return repository.getCourseById(courseId)
    }
}