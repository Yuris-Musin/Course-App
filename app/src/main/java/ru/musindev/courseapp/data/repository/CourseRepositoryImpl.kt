package ru.musindev.courseapp.data.repository

import android.content.Context
import android.content.SharedPreferences
import ru.musindev.courseapp.data.model.CourseDto
import ru.musindev.courseapp.data.source.CoursesRemoteDataSource
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CoursesRemoteDataSource,
    private val context: Context
) : CourseRepository {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("course_prefs", Context.MODE_PRIVATE)
    }

    private val favoritesKey = "favorites"

    override suspend fun getCourses(): Result<List<Course>> {
        return remoteDataSource.fetchCourses().map { dtoList ->
            val favorites = getFavoriteIds()
            dtoList.map { dto ->
                dto.toDomain(favorites.contains(dto.id))
            }
        }
    }

    override suspend fun toggleFavorite(courseId: Int): Result<Unit> {
        return try {
            val favorites = getFavoriteIds().toMutableSet()
            if (favorites.contains(courseId)) {
                favorites.remove(courseId)
            } else {
                favorites.add(courseId)
            }
            saveFavoriteIds(favorites)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteCourses(): Result<List<Course>> {
        return remoteDataSource.fetchCourses().map { dtoList ->
            val favorites = getFavoriteIds()
            dtoList
                .filter { favorites.contains(it.id) }
                .map { dto -> dto.toDomain(true) }
        }
    }

    private fun getFavoriteIds(): Set<Int> {
        val favoritesString = sharedPreferences.getString(favoritesKey, "") ?: ""
        return if (favoritesString.isEmpty()) {
            emptySet()
        } else {
            favoritesString.split(",").mapNotNull { it.toIntOrNull() }.toSet()
        }
    }

    private fun saveFavoriteIds(favorites: Set<Int>) {
        val favoritesString = favorites.joinToString(",")
        sharedPreferences.edit().putString(favoritesKey, favoritesString).apply()
    }

    private fun CourseDto.toDomain(isFavorite: Boolean): Course {
        return Course(
            id = this.id,
            title = this.title,
            description = this.text,
            price = this.price,
            rating = this.rate.toDoubleOrNull() ?: 0.0,
            startDate = this.startDate,
            isFavorite = isFavorite,
            publishDate = this.publishDate
        )
    }
}