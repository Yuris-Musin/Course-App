package ru.musindev.courseapp.domain.usecase

import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val courseRepository: CourseRepository
) {
    suspend operator fun invoke(courseId: Int): Result<Unit> {
        return courseRepository.toggleFavorite(courseId)
    }
}
