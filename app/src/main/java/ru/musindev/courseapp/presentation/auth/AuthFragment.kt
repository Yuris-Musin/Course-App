package ru.musindev.courseapp.presentation.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import ru.musindev.courseapp.R
import ru.musindev.courseapp.databinding.FragmentAuthBinding
import ru.musindev.courseapp.presentation.activity.MainActivity

class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filterNoCyrillic = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("[a-zA-Z0-9@._\\-]+")
            if (source.matches(pattern)) null else ""
        }
        binding.etEmail.filters = arrayOf(filterNoCyrillic)

        fun validateInput() {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            // Проверяем email по стандартному паттерну и пароль не пустой
            val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            binding.btnSignIn.isEnabled = isEmailValid && password.isNotEmpty()
        }

        // Слушатели на изменение текста
        binding.etEmail.doOnTextChanged { _, _, _, _ -> validateInput() }
        binding.etPassword.doOnTextChanged { _, _, _, _ -> validateInput() }

        // Изначально деактивируем кнопку
        binding.btnSignIn.isEnabled = false

        binding.btnSignInVk.setOnClickListener {
            openWebsite(requireContext(), "https://vk.com")
        }

        binding.btnSignInOk.setOnClickListener {
            openWebsite(requireContext(), "https://ok.ru")
        }

        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_homeFragment)
        }

        (activity as MainActivity).setUIVisibility(showBottomNav = false)
    }

    fun openWebsite(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = url.toUri()
        }
        context.startActivity(intent)
    }
}