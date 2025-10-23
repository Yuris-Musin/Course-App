package ru.musindev.courseapp

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.musindev.courseapp.di.DaggerAppComponent

class App : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<App> =
        DaggerAppComponent.builder().withContext(applicationContext).build()
}