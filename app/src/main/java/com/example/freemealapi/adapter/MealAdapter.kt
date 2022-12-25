package com.example.freemealapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.freemealapi.R
import com.example.freemealapi.databinding.RvSubCategoryItemBinding
import com.example.freemealapi.models.Meal

class MealAdapter: RecyclerView.Adapter<MealAdapter.MealViewHolder>() {

    var mealList = mutableListOf<Meal>()

    inner class MealViewHolder(val binding: RvSubCategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(RvSubCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(mealList[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_food_img)
            .into(holder.binding.imgRecipeImage)

        holder.binding.tvRecipeName.text = mealList[position].strMeal
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    fun setMealData(meal: MutableList<Meal>){
        val mealDiffUtil = MealDiffUtil(mealList, meal)
        val mealDiffResult = DiffUtil.calculateDiff(mealDiffUtil)
        this.mealList = meal
        mealDiffResult.dispatchUpdatesTo(this)
    }
}