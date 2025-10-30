package ru.musindev.courseapp.domain.model

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val rating: String,
    val startDate: String,
    val isFavorite: Boolean,
    val publishDate: String
)