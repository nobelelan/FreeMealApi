package com.example.freemealapi.ui

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemealapi.adapter.CategoryAdapter
import com.example.freemealapi.adapter.MealAdapter
import com.example.freemealapi.api.RetrofitInstance
import com.example.freemealapi.databinding.ActivityHomeBinding
import com.example.freemealapi.models.CategoriesResponse
import com.example.freemealapi.models.MealsResponse
import com.example.freemealapi.repository.MealRepository
import com.example.freemealapi.utils.Resource
import com.example.freemealapi.viewmodel.MealViewModel
import com.example.freemealapi.viewmodel.MealViewModelProviderFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var mealViewModel: MealViewModel

    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val mealAdapter: MealAdapter by lazy { MealAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpCategoryRecyclerView()

        setUpMealRecyclerView()

        val repository = MealRepository()
        val mealViewModelProviderFactory = MealViewModelProviderFactory(applicationContext as Application, repository)
        mealViewModel = ViewModelProvider(this, mealViewModelProviderFactory)[MealViewModel::class.java]


        mealViewModel.mealCategories.observe(this, Observer { response ->
            handleCategoriesResponse(response)
        })
        mealViewModel.meals.observe(this, Observer { response ->
            handleMealsResponse(response)
        })

        categoryAdapter.setClickListener(categoryClickListener)

    }

    val categoryClickListener = object: CategoryAdapter.OnItemClickListener{
        override fun onClicked(categoryName: String) {
            mealViewModel.getMealOnCategory(categoryName)
        }
    }

    private fun handleMealsResponse(response: Resource<MealsResponse>) {
        when(response){
            is Resource.Success ->{
                response.data?.let { mealsResponse ->
                    mealAdapter.setMealData(mealsResponse.meals!!.toMutableList())
                }
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Loading -> {}
        }
    }

    private fun handleCategoriesResponse(response: Resource<CategoriesResponse>) {
        when(response){
            is Resource.Success ->{
                response.data?.let { categoriesResponse ->
                    categoryAdapter.setCategoryData(categoriesResponse.categories!!.toMutableList())
                    mealViewModel.getMealOnCategory(categoriesResponse.categories.first().strCategory)
                }
            }
            is Resource.Error -> {
                response.message?.let { message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Loading -> {}
        }
    }

    private fun setUpMealRecyclerView() {
        val mealRecyclerView = binding.rvSubCategory
        mealRecyclerView.adapter = mealAdapter
        mealRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setUpCategoryRecyclerView() {
        val categoryRecyclerView = binding.rvMainCategory
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
    }
}