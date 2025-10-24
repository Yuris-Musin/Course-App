package ru.musindev.courseapp.presentation.home.adapter

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.musindev.courseapp.domain.model.Course

class CoursesAdapter(
    onCourseClick: (Course) -> Unit,
    onFavoriteClick: (Int) -> Unit
) : ListDelegationAdapter<List<Course>>() {

    init {
        delegatesManager.addDelegate(
            CourseAdapterDelegate(onCourseClick, onFavoriteClick)
        )
    }

    fun submitList(courses: List<Course>) {
        items = courses
        notifyDataSetChanged()
    }
}
