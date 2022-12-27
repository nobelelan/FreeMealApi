package com.example.freemealapi.models

import com.google.gson.annotations.SerializedName

data class MealIngredientsResponse(
    @SerializedName("meals")
    val mealIngredients: List<MealIngredients>?
)