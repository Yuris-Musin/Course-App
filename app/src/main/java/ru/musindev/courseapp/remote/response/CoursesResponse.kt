package ru.musindev.courseapp.remote.response

import com.google.gson.annotations.SerializedName
import ru.musindev.courseapp.data.model.CourseDto

data class CoursesResponse(
    @SerializedName("courses")
    val courses: List<CourseDto>
)