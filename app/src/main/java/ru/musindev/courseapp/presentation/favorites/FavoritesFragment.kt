package ru.musindev.courseapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.musindev.courseapp.core.base.BaseFragment
import ru.musindev.courseapp.databinding.FragmentFavoritesBinding
import ru.musindev.courseapp.presentation.home.adapter.CoursesAdapter
import javax.inject.Inject

class FavoritesFragment : BaseFragment<FragmentFavoritesBinding>() {

    private lateinit var viewModel: FavoritesViewModel

    @Inject
    lateinit var vmFactory: FavoritesViewModel.Factory

    private lateinit var adapter: CoursesAdapter

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoritesBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, vmFactory)[FavoritesViewModel::class.java]

        setupAdapter()

        collectViewModel()

        viewModel.loadFavoriteCourses()
    }

    private fun setupAdapter() {
        adapter = CoursesAdapter(
            onCourseClick = { course ->
                Toast.makeText(context, "Курс: ${course.title}", Toast.LENGTH_SHORT).show()
            },
            onFavoriteClick = { courseId ->
                viewModel.toggleFavorite(courseId)
            }
        )
        binding.rvFavoriteCourses.adapter = adapter
    }

    private fun collectViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteCourses.collect { courses ->
                if (courses.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvFavoriteCourses.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvFavoriteCourses.visibility = View.VISIBLE
                    adapter.submitList(courses)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect {isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collect { error ->
                error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }

            }
        }

    }

}
