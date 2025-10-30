package ru.musindev.courseapp.data.dbo.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.musindev.courseapp.data.dbo.entity.CourseEntity

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    suspend fun getAll(): List<CourseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<CourseEntity>)

    @Query("DELETE FROM courses")
    suspend fun clear()
}