package ru.musindev.courseapp.data.repository

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.musindev.courseapp.data.NetworkUtil
import ru.musindev.courseapp.data.dbo.dao.CourseDao
import ru.musindev.courseapp.data.dbo.entity.CourseEntity
import ru.musindev.courseapp.data.model.CourseDto
import ru.musindev.courseapp.data.source.CoursesRemoteDataSource
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.repository.CourseRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CoursesRemoteDataSource,
    private val courseDao: CourseDao,
    private val context: Context,
    private val networkUtil: NetworkUtil
) : CourseRepository {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("course_prefs", Context.MODE_PRIVATE)
    }

    private val favoritesKey = "favorites"

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getCourses(): Result<List<Course>> {
        // 1. Пытаемся загрузить из кеша
        val cachedEntities = courseDao.getAll()
        val cachedCourses = if (cachedEntities.isNotEmpty()) {
            val favorites = getFavoriteIds()
            cachedEntities.map { it.toDomain(favorites.contains(it.id)) }
        } else {
            emptyList()
        }

        // 2. Если кеш есть — сразу возвращаем его
        if (cachedCourses.isNotEmpty()) {
            // Запускаем фоновое обновление, если есть интернет
            if (networkUtil.isOnline()) {
                fetchAndCacheCoursesInBackground()
            }
            return Result.success(cachedCourses)
        }

        // 3. Если кеш пуст — грузим из сети
        return if (networkUtil.isOnline()) {
            fetchAndCacheCoursesFromNetwork()
        } else {
            Result.failure(Exception("Нет интернета и нет кеша"))
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

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override suspend fun getFavoriteCourses(): Result<List<Course>> {
        // Получаем курсы из кеша (или из сети, если кеш пуст — но лучше сначала обновить)
        val allCoursesResult = getCourses()
        return if (allCoursesResult.isSuccess) {
            val favorites = getFavoriteIds()
            val favoriteCourses = allCoursesResult.getOrNull()?.filter { favorites.contains(it.id) } ?: emptyList()
            Result.success(favoriteCourses)
        } else {
            Result.failure(allCoursesResult.exceptionOrNull() ?: Exception("Ошибка загрузки курсов"))
        }
    }

    // === Вспомогательные методы ===

    private suspend fun fetchAndCacheCoursesFromNetwork(): Result<List<Course>> {
        return remoteDataSource.fetchCourses().mapCatching { dtoList ->
            val entities = dtoList.map { it.toEntity() }
            courseDao.insertAll(entities)

            val favorites = getFavoriteIds()
            dtoList.map { dto -> dto.toDomain(favorites.contains(dto.id)) }
        }
    }

    private fun fetchAndCacheCoursesInBackground() {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                remoteDataSource.fetchCourses().getOrThrow()
            }.onSuccess { dtoList ->
                val entities = dtoList.map { it.toEntity() }
                courseDao.insertAll(entities)
                // Опционально: отправить событие об обновлении (например, через SharedFlow)
            }.onFailure { /* логируем ошибку */ }
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

    private fun CourseDto.toEntity(): CourseEntity {
        return CourseEntity(
            id = this.id,
            title = this.title,
            text = this.text,
            price = this.price,
            rate = this.rate,
            startDate = this.startDate,
            publishDate = this.publishDate,
            isFavorite = isFavorite,
            lastUpdated = System.currentTimeMillis()
        )
    }

    private fun CourseEntity.toDomain(isFavorite: Boolean): Course {
        return Course(
            id = this.id,
            title = this.title,
            description = this.text,
            price = this.price,
            rating = this.rate,
            startDate = this.startDate,
            isFavorite = isFavorite,
            publishDate = this.publishDate
        )
    }

    private fun CourseDto.toDomain(isFavorite: Boolean): Course {
        return Course(
            id = this.id,
            title = this.title,
            description = this.text,
            price = this.price,
            rating = this.rate, // или rate.toDoubleOrNull() ?: 0.0, если rating: Double
            startDate = this.startDate,
            isFavorite = isFavorite,
            publishDate = this.publishDate
        )
    }
}