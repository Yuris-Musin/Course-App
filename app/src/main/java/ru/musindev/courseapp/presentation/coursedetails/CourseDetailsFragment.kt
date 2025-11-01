package ru.musindev.courseapp.presentation.coursedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.musindev.courseapp.core.base.BaseFragment
import ru.musindev.courseapp.databinding.FragmentCourseDetailsBinding
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class CourseDetailsFragment : BaseFragment<FragmentCourseDetailsBinding>() {

    private lateinit var viewModel: CourseDetailsViewModel

    @Inject
    lateinit var vmFactory: CourseDetailsViewModel.Factory

    override fun inflaterViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCourseDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val courseId = arguments?.getInt("courseId")
        if (courseId == null) {
            findNavController().popBackStack()
            return
        }

        viewModel = ViewModelProvider(this, vmFactory)[CourseDetailsViewModel::class.java]

        viewModel.loadCourse(courseId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is CourseDetailsState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is CourseDetailsState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        with(binding) {
                            tvCourseTitle.text = state.course.title
                            tvCourseDescription.text = state.course.description
                            tvCourseRating.text = state.course.rating
                            tvStartDate.text = formatDate(state.course.startDate)
                        }
                    }
                    is CourseDetailsState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        dateString
    }
}