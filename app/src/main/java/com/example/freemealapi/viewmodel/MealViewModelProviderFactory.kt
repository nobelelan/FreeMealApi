package com.example.freemealapi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freemealapi.repository.MealRepository

class MealViewModelProviderFactory(
    private val application: Application,
    private val mealRepository: MealRepository
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(application, mealRepository) as T
    }
}