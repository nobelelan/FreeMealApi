package com.example.freemealapi.repository

import com.example.freemealapi.api.RetrofitInstance

class MealRepository {

    fun getAllMealCategories() = RetrofitInstance.mealApi.getAllMealCategories()

    fun getMealOnCategory(categoryName: String) = RetrofitInstance.mealApi.getMealOnCategory(categoryName)

    fun getMealDetailsOnMealId(mealId: String) = RetrofitInstance.mealApi.getMealDetailsOnMealId(mealId)

    fun getSpecificMealOnName(mealName: String) = RetrofitInstance.mealApi.getSpecificMealOnName(mealName)

}