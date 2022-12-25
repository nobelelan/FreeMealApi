package com.example.freemealapi.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.freemealapi.models.CategoriesResponse
import com.example.freemealapi.models.MealIngredientsResponse
import com.example.freemealapi.models.MealsResponse
import com.example.freemealapi.repository.MealRepository
import com.example.freemealapi.utils.Resource
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
    var mealCategoriesResponse: CategoriesResponse? = null

    private val _mealLiveData = MutableLiveData<Resource<MealsResponse>>()
    val meals: LiveData<Resource<MealsResponse>> = _mealLiveData
    var mealsResponse: MealsResponse? = null

    private val _ingredientsLiveData = MutableLiveData<Resource<MealIngredientsResponse>>()
    val ingredients: LiveData<Resource<MealIngredientsResponse>> = _ingredientsLiveData
    var ingredientsResponse: MealIngredientsResponse? = null

    init {
//        getAllMealCategories()
    }


//    fun getAllMealCategories() = viewModelScope.launch {
//        _categoryLiveData.postValue(Resource.Loading())
//        try {
//            val response = mealRepository.getAllMealCategories()
//            if (response.isSuccessful){
//                response.body()?.let { resultResponse ->
//                    if (mealCategoriesResponse == null){
//                        mealCategoriesResponse = resultResponse
//                    }else{
//                        val oldCategories = mealCategoriesResponse?.categories as MutableList
//                        val newCategories = resultResponse.categories
//                        oldCategories.addAll(newCategories!!)
//                    }
//                    _categoryLiveData.postValue(Resource.Success(mealCategoriesResponse ?: resultResponse))
//                }
//            }
//            _categoryLiveData.postValue(Resource.Error("Request not successful!"))
//        }catch (exception: Exception){
//            _categoryLiveData.postValue(Resource.Error("Something went wrong!"))
//        }
//    }

    fun getMealOnCategory(categoryName: String) = viewModelScope.launch {
        _mealLiveData.postValue(Resource.Loading())
        try {
            val response = mealRepository.getMealOnCategory(categoryName)
            if (response.isSuccessful){
                response.body()?.let { resultResponse ->
                    if (mealsResponse == null){
                        mealsResponse = resultResponse
                    }else{
                        val oldMeals = mealsResponse?.meals as MutableList
                        val newMeals = resultResponse.meals
                        oldMeals.addAll(newMeals!!)
                    }
                    _mealLiveData.postValue(Resource.Success(mealsResponse ?: resultResponse))
                }
            }
            _mealLiveData.postValue(Resource.Error("Request not successful!"))
        }catch(exception: Exception){
            _mealLiveData.postValue(Resource.Error("Something went wrong!"))
        }
    }

    fun getMealDetailsOnMealId(id: String) = viewModelScope.launch {
        _ingredientsLiveData.postValue(Resource.Loading())
        try {
            val response = mealRepository.getMealDetailsOnMealId(id)
            if (response.isSuccessful){
                response.body()?.let { resultResponse ->
                    if (ingredientsResponse == null){
                        ingredientsResponse = resultResponse
                    }else{
                        val oldIngredients = ingredientsResponse?.mealIngredients as MutableList
                        val newIngredients = resultResponse.mealIngredients
                        oldIngredients.addAll(newIngredients)
                    }
                    _ingredientsLiveData.postValue(Resource.Success(ingredientsResponse ?: resultResponse))
                }
            }
            _ingredientsLiveData.postValue(Resource.Error("Request not successful!"))
        }catch (exception: Exception){
            _ingredientsLiveData.postValue(Resource.Error("Something went wrong!"))
        }
    }
}