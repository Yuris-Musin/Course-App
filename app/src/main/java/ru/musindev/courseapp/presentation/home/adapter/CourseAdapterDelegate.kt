package ru.musindev.courseapp.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import ru.musindev.courseapp.R
import ru.musindev.courseapp.databinding.ItemCourseBinding
import ru.musindev.courseapp.domain.model.Course

class CourseAdapterDelegate(
    private val onCourseClick: (Course) -> Unit,
    private val onFavoriteClick: (Int) -> Unit
) : AdapterDelegate<List<Course>>() {

    override fun isForViewType(items: List<Course>, position: Int): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemCourseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CourseViewHolder(binding, onCourseClick, onFavoriteClick)
    }

    override fun onBindViewHolder(
        items: List<Course>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        (holder as CourseViewHolder).bind(items[position])
    }

    class CourseViewHolder(
        private val binding: ItemCourseBinding,
        private val onCourseClick: (Course) -> Unit,
        private val onFavoriteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            binding.apply {
                tvCourseTitle.text = course.title
                tvCourseDescription.text = course.description
                tvCoursePrice.text = "${course.price} ₽"
                tvCourseRating.text = course.rating.toString()
                tvStartDate.text = course.startDate

                // Установка иконки избранного
                ivFavorite.setImageResource(
                    if (course.isFavorite) {
                        R.drawable.ic_favorites_selected
                    } else {
                        R.drawable.ic_favorites_unselected
                    }
                )

                // Обработчики кликов
                root.setOnClickListener { onCourseClick(course) }
                ivFavorite.setOnClickListener { onFavoriteClick(course.id) }
            }
        }
    }
}
