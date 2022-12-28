package com.example.freemealapi.repository

import androidx.lifecycle.LiveData
import com.example.freemealapi.api.RetrofitInstance
import com.example.freemealapi.db.MealIngredientsDao
import com.example.freemealapi.models.MealIngredients

class MealRepository(
    private val mealIngredientsDao: MealIngredientsDao
) {

    fun getAllMealCategories() = RetrofitInstance.mealApi.getAllMealCategories()

    fun getMealOnCategory(categoryName: String) = RetrofitInstance.mealApi.getMealOnCategory(categoryName)

    fun getMealDetailsOnMealId(mealId: String) = RetrofitInstance.mealApi.getMealDetailsOnMealId(mealId)

    fun getSpecificMealOnName(mealName: String) = RetrofitInstance.mealApi.getSpecificMealOnName(mealName)

    suspend fun upsert(mealIngredients: MealIngredients) = mealIngredientsDao.upsert(mealIngredients)

    fun getAllMealIngredients(): LiveData<MutableList<MealIngredients>> = mealIngredientsDao.getAllMealIngredients()
}