package ru.musindev.courseapp.data.dbo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.musindev.courseapp.data.dbo.dao.CourseDao
import ru.musindev.courseapp.data.dbo.entity.CourseEntity

@Database(
    entities = [CourseEntity::class],
    version = 1
)

abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
}