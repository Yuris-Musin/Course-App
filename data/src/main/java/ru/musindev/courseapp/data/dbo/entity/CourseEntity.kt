package ru.musindev.courseapp.data.dbo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val text: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String,
    val lastUpdated: Long
)