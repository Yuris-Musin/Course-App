package ru.musindev.courseapp.di.modules

import android.content.Context
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room
import dagger.Module
import ru.musindev.courseapp.data.dbo.db.CourseDatabase

@Module
class DataBaseModule {

    @Singleton
    @Provides
    fun provideDb(context: Context) = Room.databaseBuilder(
        context, CourseDatabase::class.java, "course_db"
    ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideCourseDao(context: Context) = provideDb(context).courseDao()
}