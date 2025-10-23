package ru.musindev.courseapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import ru.musindev.courseapp.App
import ru.musindev.courseapp.di.modules.MainModule
import javax.inject.Singleton

@Singleton
@Component(
    //Внедряем все модули, нужные для этого компонента
    modules = [
        AndroidInjectionModule::class,
        MainModule::class,
//        AppModule::class,
//        DatabaseModule::class,
//        DataModule::class,
//        DomainModule::class,
//        MappersModule::class,
//        RemoteModule::class,
//        SharedPreferencesModule::class,
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