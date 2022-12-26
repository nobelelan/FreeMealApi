package com.example.freemealapi.viewmodel

import android.app.Application
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.freemealapi.adapter.CategoryAdapter
import com.example.freemealapi.models.CategoriesResponse
import com.example.freemealapi.models.MealIngredientsResponse
import com.example.freemealapi.models.MealsResponse
import com.example.freemealapi.repository.MealRepository
import com.example.freemealapi.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    application: Application,
    val mealRepository: MealRepository
): AndroidViewModel(application) {

    private val _categoryLiveData = MutableLiveData<Resource<CategoriesResponse>>()
    val mealCategories: LiveData<Resource<CategoriesResponse>> = _categoryLiveData

    private val _mealLiveData = MutableLiveData<Resource<MealsResponse>>()
    val meals: LiveData<Resource<MealsResponse>> = _mealLiveData

    private val _ingredientsLiveData = MutableLiveData<Resource<MealIngredientsResponse>>()
    val ingredients: LiveData<Resource<MealIngredientsResponse>> = _ingredientsLiveData

    init {
        getAllMealCategories()


    }


    private fun getAllMealCategories() {
        _categoryLiveData.postValue(Resource.Loading())
        val response = mealRepository.getAllMealCategories()
        response.enqueue(object : Callback<CategoriesResponse> {
            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                _categoryLiveData.postValue(Resource.Error("Request not successful!"))
            }

            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: Response<CategoriesResponse>
            ) {
                _categoryLiveData.postValue(Resource.Success(response.body()!!))
            }

        })
    }

    fun getMealOnCategory(categoryName: String) {
        _mealLiveData.postValue(Resource.Loading())
        val response = mealRepository.getMealOnCategory(categoryName)
        response.enqueue(object : Callback<MealsResponse> {
            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                _mealLiveData.postValue(Resource.Error("Request not successful!"))
            }

            override fun onResponse(
                call: Call<MealsResponse>,
                response: Response<MealsResponse>
            ) {
                _mealLiveData.postValue(Resource.Success(response.body()!!))
            }

        })
    }


    fun getMealDetailsOnMealId(id: String) {
        _ingredientsLiveData.postValue(Resource.Loading())
        val response = mealRepository.getMealDetailsOnMealId(id)
        response.enqueue(object : Callback<MealIngredientsResponse> {
            override fun onFailure(call: Call<MealIngredientsResponse>, t: Throwable) {
                _ingredientsLiveData.postValue(Resource.Error("Request not successful!"))
            }

            override fun onResponse(
                call: Call<MealIngredientsResponse>,
                response: Response<MealIngredientsResponse>
            ) {
                _ingredientsLiveData.postValue(Resource.Success(response.body()!!))
            }

        })
    }
}