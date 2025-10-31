package ru.musindev.courseapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.musindev.courseapp.App
import ru.musindev.courseapp.di.modules.AppModule
import ru.musindev.courseapp.di.modules.DataBaseModule
import ru.musindev.courseapp.di.modules.MainModule
import ru.musindev.courseapp.di.modules.NetworkModule
import ru.musindev.courseapp.di.modules.DataModule
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        DataBaseModule::class,
        MainModule::class,
        NetworkModule::class,
        DataModule::class,
    ]
)

interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withContext(context: Context): Builder
        fun build(): AppComponent
    }
}