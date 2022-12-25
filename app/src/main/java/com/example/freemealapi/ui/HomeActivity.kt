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

        val categoryRecyclerView = binding.rvMainCategory
        categoryRecyclerView.adapter = categoryAdapter
        categoryRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)

        val mealRecyclerView = binding.rvSubCategory
        mealRecyclerView.adapter = mealAdapter
        mealRecyclerView.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)

        val repository = MealRepository()
        val mealViewModelProviderFactory = MealViewModelProviderFactory(applicationContext as Application, repository)
        mealViewModel = ViewModelProvider(this, mealViewModelProviderFactory)[MealViewModel::class.java]
//
//
//        mealViewModel.mealCategories.observe(this, Observer { response ->
//            when(response){
//                is Resource.Success ->{
//                    response.data?.let { categoriesResponse ->
//                        categoryAdapter.setCategoryData(categoriesResponse.categories!!.toMutableList())
//                    }
//                }
//                is Resource.Error -> {
//                    response.message?.let { message ->
//                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//                is Resource.Loading -> {}
//            }
//        })
        getCategories()


    }

    fun getCategories() {

        RetrofitInstance.mealApi.getAllMealCategories().enqueue(object : Callback<CategoriesResponse> {
            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "faidl", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: Response<CategoriesResponse>
            ) {
                Toast.makeText(this@HomeActivity, "ssss", Toast.LENGTH_SHORT).show()
                categoryAdapter.setCategoryData(response.body()!!.categories as MutableList)
            }

        })
    }
}