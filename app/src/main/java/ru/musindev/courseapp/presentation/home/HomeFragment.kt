package ru.musindev.courseapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.musindev.courseapp.core.base.BaseFragment
import ru.musindev.courseapp.databinding.FragmentHomeBinding
import ru.musindev.courseapp.presentation.activity.MainActivity
import ru.musindev.courseapp.presentation.home.adapter.CoursesAdapter
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private lateinit var viewModel: HomeViewModel

    @Inject
    lateinit var vmFactory: HomeViewModel.Factory

    private lateinit var adapter: CoursesAdapter

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =
            ViewModelProvider(this, vmFactory)[HomeViewModel::class.java]

        (activity as MainActivity).setUIVisibility(showBottomNav = true)

        setupAdapter()

        setupSortButton()

        collectViewModel()

        viewModel.loadCourses()
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
        binding.rvCourses.adapter = adapter
    }

    private fun setupSortButton() {
        binding.sort.setOnClickListener {
            viewModel.toggleSorting()
        }
    }

    private fun collectViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.courses.collect { courses ->
                adapter.submitList(courses)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isSortedDescending.collect { isDescending ->
                updateSortIcon(isDescending)
            }
        }

    }

    private fun updateSortIcon(isDescending: Boolean) {
        binding.ivSortIcon.rotationX = if (isDescending) 0f else 180f
    }

}
