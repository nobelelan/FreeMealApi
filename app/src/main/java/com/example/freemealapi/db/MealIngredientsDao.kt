package com.example.freemealapi.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.freemealapi.models.MealIngredients

@Dao
interface MealIngredientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(mealIngredients: MealIngredients)

    @Query("SELECT * FROM meal_ingredients_table ORDER BY ID DESC")
    fun getAllMealIngredients(): LiveData<MutableList<MealIngredients>>

    @Delete
    suspend fun deleteSingleMealIngredients(mealIngredients: MealIngredients)
}