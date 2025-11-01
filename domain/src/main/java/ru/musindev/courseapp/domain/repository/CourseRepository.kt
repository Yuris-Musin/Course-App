package ru.musindev.courseapp.domain.repository

import ru.musindev.courseapp.domain.model.Course

interface CourseRepository {
    suspend fun getCourses(): Result<List<Course>>
    suspend fun toggleFavorite(courseId: Int): Result<Unit>
    suspend fun getFavoriteCourses(): Result<List<Course>>
    suspend fun getCourseById(id: Int): Result<Course>
}