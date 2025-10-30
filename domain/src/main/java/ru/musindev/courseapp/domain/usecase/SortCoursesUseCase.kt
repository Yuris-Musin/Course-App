package ru.musindev.courseapp.domain.usecase

import ru.musindev.courseapp.domain.model.Course
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SortCoursesUseCase @Inject constructor() {
    operator fun invoke(
        courses: List<Course>,
        descending: Boolean
    ): List<Course> {
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        fun asTimestamp(course: Course): Long {
            return try {
                dateFormat.parse(course.startDate)?.time ?: 0L
            } catch (e: Exception) {
                0L
            }
        }

        return if (descending) {
            courses.sortedByDescending { asTimestamp(it) }
        } else {
            courses.sortedBy { asTimestamp(it) }
        }
    }
}
