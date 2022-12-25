package com.example.freemealapi.repository

import com.example.freemealapi.api.RetrofitInstance

class MealRepository {

    suspend fun getAllMealCategories() =
        RetrofitInstance.mealApi.getAllMealCategories()

    suspend fun getMealOnCategory(categoryName: String) =
        RetrofitInstance.mealApi.getMealOnCategory(categoryName)

    suspend fun getMealDetailsOnMealId(mealId: String) =
        RetrofitInstance.mealApi.getMealDetailsOnMealId(mealId)

}