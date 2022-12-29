package com.example.freemealapi.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.freemealapi.R
import com.example.freemealapi.databinding.ActivityDetailsBinding
import com.example.freemealapi.db.MealIngredientsDatabase
import com.example.freemealapi.repository.MealRepository
import com.example.freemealapi.utils.Resource
import com.example.freemealapi.viewmodel.MealViewModel
import com.example.freemealapi.viewmodel.MealViewModelProviderFactory

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private lateinit var mealViewModel: MealViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealIngredientsDao = MealIngredientsDatabase.getDatabase(this).mealIngredientsDao()
        val mealRepository = MealRepository(mealIngredientsDao)
        val mealViewModelProviderFactory =
            MealViewModelProviderFactory(applicationContext as Application, mealRepository)
        mealViewModel =
            ViewModelProvider(this, mealViewModelProviderFactory)[MealViewModel::class.java]
        val categoryMealId = intent.getStringExtra("categoryMealId")
        val searchMealId = intent.getStringExtra("searchMealId")
        categoryMealId?.let {
            mealViewModel.getMealDetailsOnMealId(categoryMealId)
        }
        searchMealId?.let {
            mealViewModel.getMealDetailsOnMealId(searchMealId)
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        mealViewModel.ingredients.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data.let { ingredientsResponse ->
                        binding.progressBar.visibility = View.GONE
                        val ingredientsList = ingredientsResponse?.mealIngredients
                        ingredientsList?.let { mealIngredients ->
                            val ingredients = mealIngredients[0]

                            binding.fabSaveMeal.setOnClickListener {
                                mealViewModel.upsert(mealIngredients[0])
                            }

                            Glide.with(this)
                                .load(ingredients.strMealThumb)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .error(R.drawable.default_food_img)
                                .into(binding.imgMealImage)

                            binding.txtYtWatch.setOnClickListener {
                                val intent = Intent(this, YoutubeWebActivity::class.java)
                                intent.putExtra("ytUrl", ingredients.strYoutube)
                                startActivity(intent)
                            }

                            binding.txtMealName.text = ingredients.strMeal
                            binding.txtAreaName.text = ingredients.strArea
                            binding.txtCategory.text = ingredients.strCategory

                            binding.txtInstructions.text = ingredients.strInstructions

                            binding.txtIngredients.text = "${ingredients.strIngredient1}       ${ingredients.strMeasure1}\n" +
                                        "${ingredients.strIngredient2}      ${ingredients.strMeasure2}\n" +
                                        "${ingredients.strIngredient3}      ${ingredients.strMeasure3}\n" +
                                        "${ingredients.strIngredient4}      ${ingredients.strMeasure4}\n" +
                                        "${ingredients.strIngredient5}      ${ingredients.strMeasure5}\n" +
                                        "${ingredients.strIngredient6}      ${ingredients.strMeasure6}\n" +
                                        "${ingredients.strIngredient7}      ${ingredients.strMeasure7}\n" +
                                        "${ingredients.strIngredient8}      ${ingredients.strMeasure8}\n" +
                                        "${ingredients.strIngredient9}      ${ingredients.strMeasure9}\n" +
                                        "${ingredients.strIngredient10}      ${ingredients.strMeasure10}\n" +
                                        "${ingredients.strIngredient11}      ${ingredients.strMeasure11}\n" +
                                        "${ingredients.strIngredient12}      ${ingredients.strMeasure12}\n" +
                                        "${ingredients.strIngredient13}      ${ingredients.strMeasure13}\n" +
                                        "${ingredients.strIngredient14}      ${ingredients.strMeasure14}\n" +
                                        "${ingredients.strIngredient15}      ${ingredients.strMeasure15}\n" +
                                        "${ingredients.strIngredient16}      ${ingredients.strMeasure16}\n" +
                                        "${ingredients.strIngredient17}      ${ingredients.strMeasure17}\n" +
                                        "${ingredients.strIngredient18}      ${ingredients.strMeasure18}\n" +
                                        "${ingredients.strIngredient19}      ${ingredients.strMeasure19}\n" +
                                        "${ingredients.strIngredient20}      ${ingredients.strMeasure20}\n"
                        }
                    }
                }
                is Resource.Error -> {
                    response.data.let { message ->
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@DetailsActivity, message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })

    }
}