package ru.musindev.courseapp.presentation.coursedetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.musindev.courseapp.domain.usecase.GetCourseByIdUseCase
import javax.inject.Inject

class CourseDetailsViewModel @Inject constructor(
    private val getCourseByIdUseCase: GetCourseByIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CourseDetailsState>(CourseDetailsState.Loading)
    val state: StateFlow<CourseDetailsState> = _state

    fun loadCourse(courseId: Int) {
        viewModelScope.launch {
            _state.value = CourseDetailsState.Loading
            getCourseByIdUseCase(courseId)
                .onSuccess { course ->
                    _state.value = CourseDetailsState.Success(course)
                }
                .onFailure { exception ->
                    _state.value = CourseDetailsState.Error(exception.message ?: "Ошибка загрузки")
                }
        }
    }

    class Factory @Inject constructor(
        private val getCourseByIdUseCase: GetCourseByIdUseCase
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseDetailsViewModel::class.java)) {
                return CourseDetailsViewModel(getCourseByIdUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}