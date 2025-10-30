package ru.musindev.courseapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.usecase.GetFavoriteCoursesUseCase
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _favoriteCourses = MutableSharedFlow<List<Course>>()
    val favoriteCourses: SharedFlow<List<Course>>
        get() = _favoriteCourses.asSharedFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading.asStateFlow()

    private val _error = MutableSharedFlow<String?>()
    val error: SharedFlow<String?>
        get() = _error.asSharedFlow()

    fun loadFavoriteCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.emit(null)

            getFavoriteCoursesUseCase()
                .onSuccess { coursesList ->
                    _favoriteCourses.emit(coursesList)
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.emit(exception.message ?: "Неизвестная ошибка")
                    _isLoading.value = false
                }
        }
    }

    fun toggleFavorite(courseId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(courseId)
                .onSuccess {
                    loadFavoriteCourses()
                }
                .onFailure { exception ->
                    _error.emit(exception.message ?: "Ошибка при удалении из избранного")
                }
        }
    }
}
