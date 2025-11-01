package ru.musindev.courseapp.presentation.coursedetails

import ru.musindev.courseapp.domain.model.Course

sealed class CourseDetailsState {
    object Loading : CourseDetailsState()
    data class Success(val course: Course) : CourseDetailsState()
    data class Error(val message: String) : CourseDetailsState()
}