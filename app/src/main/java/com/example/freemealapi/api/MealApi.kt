package com.example.freemealapi.api

import com.example.freemealapi.models.CategoriesResponse
import com.example.freemealapi.models.MealIngredientsResponse
import com.example.freemealapi.models.MealsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("categories.php")
     fun getAllMealCategories(): Call<CategoriesResponse>

    @GET("filter.php")
    suspend fun getMealOnCategory(
        @Query("c")
        categoryName: String
    ): Response<MealsResponse>

    @GET("lookup.php")
    suspend fun getMealDetailsOnMealId(
        @Query("i")
        mealId: String
    ): Response<MealIngredientsResponse>
}