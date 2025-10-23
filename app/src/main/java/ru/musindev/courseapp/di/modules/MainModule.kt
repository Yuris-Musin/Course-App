package ru.musindev.courseapp.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.musindev.courseapp.presentation.activity.MainActivity

@Module
interface MainModule {
    @ContributesAndroidInjector
    fun bindMainActivity(): MainActivity

}