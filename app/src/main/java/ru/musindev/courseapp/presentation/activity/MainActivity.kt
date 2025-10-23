package ru.musindev.courseapp.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.musindev.courseapp.R
import ru.musindev.courseapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        NavHostFragment.findNavController(supportFragmentManager.findFragmentById(R.id.fragment_placeholder) as NavHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {

                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.favorites -> {
                    navController.navigate(R.id.favoritesFragment)
                    true
                }

                R.id.account -> {
                    navController.navigate(R.id.accountFragment)
                    true
                }

                else -> false
            }
        }



    }

    fun setUIVisibility(showBottomNav: Boolean) {
        binding.bottomNavigation.visibility = if (showBottomNav) View.VISIBLE else View.GONE
    }
}