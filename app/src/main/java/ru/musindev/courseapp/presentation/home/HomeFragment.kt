package ru.musindev.courseapp.presentation.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import ru.musindev.courseapp.R
import ru.musindev.courseapp.databinding.FragmentFavoritesBinding
import ru.musindev.courseapp.databinding.FragmentHomeBinding
import ru.musindev.courseapp.presentation.activity.MainActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Показываем или скрываем BottomNav в зависимости от текущего фрагмента
        (activity as MainActivity).setUIVisibility(
            showBottomNav = true
        )
    }
}