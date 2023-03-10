package com.example.freemealapi.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemealapi.adapter.CategoryAdapter
import com.example.freemealapi.adapter.MealAdapter
import com.example.freemealapi.adapter.SavedRecipesAdapter
import com.example.freemealapi.adapter.SearchAdapter
import com.example.freemealapi.api.RetrofitInstance
import com.example.freemealapi.databinding.ActivityHomeBinding
import com.example.freemealapi.db.MealIngredientsDatabase
import com.example.freemealapi.models.*
import com.example.freemealapi.repository.MealRepository
import com.example.freemealapi.utils.Resource
import com.example.freemealapi.viewmodel.MealViewModel
import com.example.freemealapi.viewmodel.MealViewModelProviderFactory
import com.google.android.gms.ads.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var mealViewModel: MealViewModel

    private val categoryAdapter: CategoryAdapter by lazy { CategoryAdapter() }
    private val mealAdapter: MealAdapter by lazy { MealAdapter() }
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter() }
    private val savedRecipesAdapter: SavedRecipesAdapter by lazy { SavedRecipesAdapter() }

    private var homeBannerAdView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpCategoryRecyclerView()

        setUpMealRecyclerView()

        setUpSearchRecyclerView()

        setUpSavedRecyclerView()

        searchRecipes()

        val mealIngredientsDao = MealIngredientsDatabase.getDatabase(this).mealIngredientsDao()
        val repository = MealRepository(mealIngredientsDao)
        val mealViewModelProviderFactory = MealViewModelProviderFactory(applicationContext as Application, repository)
        mealViewModel = ViewModelProvider(this, mealViewModelProviderFactory)[MealViewModel::class.java]


        mealViewModel.mealCategories.observe(this, Observer { response ->
            handleCategoriesResponse(response)
        })
        mealViewModel.meals.observe(this, Observer { response ->
            handleMealsResponse(response)
        })
        mealViewModel.specificMealOnName.observe(this, Observer { response ->
            handleSpecificMealResponse(response)
        })


        categoryAdapter.setClickListener(categoryClickListener)
        mealAdapter.setOnClickListener(mealClickListener)
        searchAdapter.setOnClickListener(searchItemListener)
        savedRecipesAdapter.setOnClickListener(savedRecipesClickListener)



        mealViewModel.getAllMealIngredients().observe(this, Observer {
            savedRecipesAdapter.differ.submitList(it)
        })





        MobileAds.initialize(this)

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("PLACE_TEST_DEVICE_ID_1_HERE","PLACE_TEST_DEVICE_ID_2_HERE"))
                .build()
        )

        homeBannerAdView = binding.homeBannerAd

        val adRequest = AdRequest.Builder().build()
        homeBannerAdView?.loadAd(adRequest)

        homeBannerAdView?.adListener = object : AdListener(){
            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdClosed() {
                super.onAdClosed()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
            }

            override fun onAdOpened() {
                super.onAdOpened()
            }

            override fun onAdSwipeGestureClicked() {
                super.onAdSwipeGestureClicked()
            }
        }
    }

    val savedRecipesClickListener = object: SavedRecipesAdapter.OnItemClickListener{

        override fun onWatchVideoClick(strYoutube: String) {
            val intent = Intent(this@HomeActivity, YoutubeWebActivity::class.java)
            intent.putExtra("ytUrl", strYoutube)
            startActivity(intent)
        }

        override fun onDeleteClick(mealIngredients: MealIngredients) {
            mealViewModel.deleteSingleMealIngredients(mealIngredients)
        }
    }


    fun searchRecipes(){
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.isNullOrEmpty()){
                    binding.rvSearchSpecificMeal.visibility = View.INVISIBLE
                    binding.searchView.clearFocus()
                }else{
                    binding.rvSearchSpecificMeal.visibility = View.VISIBLE
                    mealViewModel.getSpecificMealOnName(query)
                    binding.searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrEmpty()){
                    binding.rvSearchSpecificMeal.visibility = View.INVISIBLE
                    binding.txtCategoriesText.visibility = View.VISIBLE
                    binding.rvMainCategory.visibility = View.VISIBLE
                    binding.txtSubCategory.visibility = View.VISIBLE
                    binding.rvSubCategory.visibility = View.VISIBLE
                    binding.searchView.clearFocus()
                }else{
                    binding.rvSearchSpecificMeal.visibility = View.VISIBLE
                    binding.txtCategoriesText.visibility = View.INVISIBLE
                    binding.rvMainCategory.visibility = View.INVISIBLE
                    binding.txtSubCategory.visibility = View.INVISIBLE
                    binding.rvSubCategory.visibility = View.INVISIBLE
                    mealViewModel.getSpecificMealOnName(newText)
                }
                return true
            }
        })
    }


    private fun handleSpecificMealResponse(response: Resource<MealIngredientsResponse>) {
        when(response){
            is Resource.Success ->{
                binding.txtCategoriesText.visibility = View.VISIBLE
                binding.txtSubCategory.visibility = View.VISIBLE
                response.data?.let { mealListResponse ->
                    mealListResponse.mealIngredients?.toMutableList()?.let {
                        searchAdapter.setSearchItemData(it)
                    }
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
            is Resource.Error -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                response.message?.let { message ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Loading -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    val categoryClickListener = object: CategoryAdapter.OnItemClickListener{
        override fun onItemClick(categoryName: String) {
            mealViewModel.getMealOnCategory(categoryName)
            setSubCategoryTextTitle(categoryName)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSubCategoryTextTitle(categoryName: String) {
        binding.txtSubCategory.text = "${categoryName.replaceFirstChar { it.uppercase() }} Categories"
    }

    val mealClickListener = object : MealAdapter.OnItemClickListener{
        override fun onItemClick(mealId: String) {

            val intent = Intent(this@HomeActivity, DetailsActivity::class.java)
            intent.putExtra("categoryMealId", mealId)
            startActivity(intent)
        }
    }

    val searchItemListener = object : SearchAdapter.OnItemClickListener{
        override fun onItemClick(mealId: String) {
            val intent = Intent(this@HomeActivity, DetailsActivity::class.java)
            intent.putExtra("searchMealId", mealId)
            startActivity(intent)
        }
    }

    private fun handleMealsResponse(response: Resource<MealsResponse>) {
        when(response){
            is Resource.Success ->{
                binding.txtCategoriesText.visibility = View.VISIBLE
                binding.txtSubCategory.visibility = View.VISIBLE
                response.data?.let { mealsResponse ->
                    binding.progressBar.visibility = View.INVISIBLE
                    mealAdapter.setMealData(mealsResponse.meals!!.toMutableList())
                }
            }
            is Resource.Error -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                response.message?.let { message ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Loading -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleCategoriesResponse(response: Resource<CategoriesResponse>) {
        when(response){
            is Resource.Success ->{
                binding.txtCategoriesText.visibility = View.VISIBLE
                binding.txtSubCategory.visibility = View.VISIBLE
                response.data?.let { categoriesResponse ->
                    categoryAdapter.setCategoryData(categoriesResponse.categories!!.toMutableList())
                    mealViewModel.getMealOnCategory(categoriesResponse.categories.first().strCategory)
                    setSubCategoryTextTitle(categoriesResponse.categories.first().strCategory)
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
            is Resource.Error -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                response.message?.let { message ->
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Loading -> {
                binding.txtCategoriesText.visibility = View.GONE
                binding.txtSubCategory.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
            }
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

    private fun setUpSearchRecyclerView(){
        val searchRecyclerView = binding.rvSearchSpecificMeal
        searchRecyclerView.adapter = searchAdapter
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
    }
    private fun setUpSavedRecyclerView(){
        val savedRecyclerView = binding.rvSavedRecipes
        savedRecyclerView.adapter = savedRecipesAdapter
        savedRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onPause() {
        homeBannerAdView?.pause()
        super.onPause()
    }

    override fun onResume() {
        homeBannerAdView?.resume()
        super.onResume()
    }

    override fun onDestroy() {
        homeBannerAdView?.destroy()
        super.onDestroy()
    }
}