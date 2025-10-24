package ru.musindev.courseapp.domain.model

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val rating: Double,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String
)