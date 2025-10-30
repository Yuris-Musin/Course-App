package ru.musindev.courseapp.domain.usecase

import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(): Result<List<Course>> {
        return courseRepository.getCourses()
    }
}
