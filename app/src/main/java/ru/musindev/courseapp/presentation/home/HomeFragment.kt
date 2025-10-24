package ru.musindev.courseapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.musindev.courseapp.R
import ru.musindev.courseapp.core.base.BaseFragment
import ru.musindev.courseapp.databinding.FragmentHomeBinding
import ru.musindev.courseapp.presentation.activity.MainActivity
import ru.musindev.courseapp.presentation.home.adapter.CoursesAdapter
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: CoursesAdapter

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        // Показываем BottomNav
        (activity as MainActivity).setUIVisibility(showBottomNav = true)

        // Настройка адаптера
        setupAdapter()

        // Настройка кнопки сортировки
        setupSortButton()

        // Наблюдение за данными
        observeViewModel()

        // Загрузка курсов
        viewModel.loadCourses()
    }

    private fun setupAdapter() {
        adapter = CoursesAdapter(
            onCourseClick = { course ->
                // Обработка клика по курсу (навигация на детальный экран)
                Toast.makeText(context, "Курс: ${course.title}", Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { courseId ->
                // Переключение избранного
                viewModel.toggleFavorite(courseId)
            }
        )
        binding.rvCourses.adapter = adapter
    }

    private fun setupSortButton() {
        binding.sort.setOnClickListener {
            viewModel.toggleSorting()
        }
    }

    private fun observeViewModel() {
        // Наблюдение за списком курсов
        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            adapter.submitList(courses)
        }

        // Наблюдение за состоянием загрузки
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        // Наблюдение за порядком сортировки
        viewModel.isSortedDescending.observe(viewLifecycleOwner) { isDescending ->
            updateSortIcon(isDescending)
        }
    }

    private fun updateSortIcon(isDescending: Boolean) {
        // Обновляем иконку в зависимости от направления сортировки
        // Если у вас есть отдельные иконки для стрелок вверх/вниз, используйте их
        // Например:
        // val iconRes = if (isDescending) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up
        // binding.ivSortIcon.setImageResource(iconRes)

        // Или можно поворачивать существующую иконку:
        binding.sort.rotation = if (isDescending) 0f else 180f

        // Также можно менять цвет для индикации активности
        val color = if (isDescending) {
            ContextCompat.getColor(requireContext(), R.color.btn_bg_color_1)
        } else {
            ContextCompat.getColor(requireContext(), android.R.color.darker_gray)
        }
        // Примените цвет к TextView или ImageView внутри sort
    }

}
