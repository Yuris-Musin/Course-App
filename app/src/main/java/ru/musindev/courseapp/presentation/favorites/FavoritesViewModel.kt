package ru.musindev.courseapp.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.musindev.courseapp.domain.model.Course
import ru.musindev.courseapp.domain.repository.CourseRepository
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import javax.inject.Inject

class FavoritesViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _favoriteCourses = MutableLiveData<List<Course>>()
    val favoriteCourses: LiveData<List<Course>> = _favoriteCourses

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadFavoriteCourses() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            courseRepository.getFavoriteCourses()
                .onSuccess { coursesList ->
                    _favoriteCourses.value = coursesList
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
                    loadFavoriteCourses()
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Ошибка при удалении из избранного"
                }
        }
    }
}
