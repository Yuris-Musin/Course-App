package ru.musindev.courseapp.di.modules

import dagger.Module
import dagger.Provides
import ru.musindev.courseapp.domain.usecase.GetCoursesUseCase
import ru.musindev.courseapp.domain.usecase.GetFavoriteCoursesUseCase
import ru.musindev.courseapp.domain.usecase.SortCoursesUseCase
import ru.musindev.courseapp.domain.usecase.ToggleFavoriteUseCase
import ru.musindev.courseapp.presentation.favorites.FavoritesViewModel
import ru.musindev.courseapp.presentation.home.HomeViewModel

@Module
class AppModule {

    @Provides
    fun provideHomeViewModelFactory(
        getCoursesUseCase: GetCoursesUseCase,
        toggleFavoriteUseCase: ToggleFavoriteUseCase,
        sortCoursesUseCase: SortCoursesUseCase
    ) = HomeViewModel.Factory(
        getCoursesUseCase = getCoursesUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        sortCoursesUseCase = sortCoursesUseCase
    )

    @Provides
    fun provideFavoriteViewModelFactory(
        getFavoriteCoursesUseCase: GetFavoriteCoursesUseCase,
        toggleFavoriteUseCase: ToggleFavoriteUseCase
    ) = FavoritesViewModel.Factory(
        getFavoriteCoursesUseCase = getFavoriteCoursesUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
    )

}