package ru.musindev.courseapp.data.model

import com.google.gson.annotations.SerializedName

data class CourseDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("text")
    val text: String,

    @SerializedName("price")
    val price: String,

    @SerializedName("rate")
    val rate: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("isFavorite")
    val isFavorite: Boolean,

    @SerializedName("publishDate")
    val publishDate: String,

    @SerializedName("lastUpdated")
    val lastUpdated: Long

)
