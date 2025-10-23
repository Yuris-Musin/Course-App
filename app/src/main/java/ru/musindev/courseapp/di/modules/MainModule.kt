package ru.musindev.courseapp.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.musindev.courseapp.presentation.account.AccountFragment
import ru.musindev.courseapp.presentation.activity.MainActivity
import ru.musindev.courseapp.presentation.auth.AuthFragment
import ru.musindev.courseapp.presentation.favorites.FavoritesFragment
import ru.musindev.courseapp.presentation.home.HomeFragment

@Module
interface MainModule {
    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    fun bindAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    fun bindFavoritesFragment(): FavoritesFragment

    @ContributesAndroidInjector
    fun bindHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    fun bindAccountFragment(): AccountFragment
}