package ru.musindev.courseapp.data.source

import ru.musindev.courseapp.data.model.CourseDto
import ru.musindev.courseapp.remote.api.CoursesApi
import javax.inject.Inject

class CoursesRemoteDataSource @Inject constructor(
    private val coursesApi: CoursesApi
) {
    suspend fun fetchCourses(): Result<List<CourseDto>> {
        return try {
            val response = coursesApi.getCourses()
            Result.success(response.courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
