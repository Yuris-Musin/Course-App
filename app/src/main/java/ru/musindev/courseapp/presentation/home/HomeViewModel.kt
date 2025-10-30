package ru.musindev.courseapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.usecase.GetCoursesUseCase
import ru.musindev.courseapp.domain.usecase.SortCoursesUseCase
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val sortCoursesUseCase: SortCoursesUseCase
) : ViewModel() {

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>>
        get() = _courses.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?>
        get() = _error.asStateFlow()

    private val _isSortedDescending = MutableStateFlow(true)
    val isSortedDescending: StateFlow<Boolean>
        get() = _isSortedDescending.asStateFlow()

    private var originalCourses: List<Course> = emptyList()

    fun loadCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            getCoursesUseCase()
                .onSuccess { coursesList ->
                    originalCourses = coursesList
                    applySorting()
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
        applySorting()
    }

    private fun applySorting() {
        val sortedList = sortCoursesUseCase.invoke(
            courses = originalCourses,
            descending = _isSortedDescending.value == true
        )
        _courses.value = sortedList
    }
}
