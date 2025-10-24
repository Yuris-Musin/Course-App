package ru.musindev.courseapp.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.usecase.GetCoursesUseCase
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Состояние сортировки: true = по убыванию, false = по возрастанию
    private val _isSortedDescending = MutableLiveData<Boolean>(true)
    val isSortedDescending: LiveData<Boolean> = _isSortedDescending

    // Хранение оригинального списка
    private var originalCourses: List<Course> = emptyList()

    fun loadCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getCoursesUseCase()
                .onSuccess { coursesList ->
                    originalCourses = coursesList
                    applySorting(coursesList)
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Неизвестная ошибка"
                    _isLoading.value = false
                }
        }
    }

    fun toggleFavorite(courseId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId)
                .onSuccess {
                    // Обновляем список курсов
                    loadCourses()
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Ошибка при добавлении в избранное"
                }
        }
    }

    fun toggleSorting() {
        val currentSortOrder = _isSortedDescending.value ?: true
        _isSortedDescending.value = !currentSortOrder
        applySorting(originalCourses)
    }

    private fun applySorting(coursesList: List<Course>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val sortedList = try {
            if (_isSortedDescending.value == true) {
                // Сортировка по убыванию (новые сначала)
                coursesList.sortedByDescending { course ->
                    try {
                        dateFormat.parse(course.publishDate)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }
            } else {
                // Сортировка по возрастанию (старые сначала)
                coursesList.sortedBy { course ->
                    try {
                        dateFormat.parse(course.publishDate)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }
            }
        } catch (e: Exception) {
            coursesList
        }

        _courses.value = sortedList
    }
}
