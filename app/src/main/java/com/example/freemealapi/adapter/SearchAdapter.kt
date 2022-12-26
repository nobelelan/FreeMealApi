package com.example.freemealapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.freemealapi.R
import com.example.freemealapi.databinding.RvSearchItemBinding
import com.example.freemealapi.models.MealIngredients

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.MealViewHolder>() {

    var specificMealList = mutableListOf<MealIngredients>()
    var searchItemListener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(mealId: String)
    }

    fun setOnClickListener(listener: OnItemClickListener){
        searchItemListener = listener
    }

    inner class MealViewHolder(val binding: RvSearchItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(RvSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(specificMealList[position].strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_food_img)
            .into(holder.binding.imgRecipeImage)

        holder.binding.tvRecipeName.text = specificMealList[position].strMeal

        holder.itemView.setOnClickListener {
            searchItemListener!!.onItemClick(specificMealList[position].idMeal)
        }
    }

    override fun getItemCount(): Int {
        return specificMealList.size
    }

    fun setSearchItemData(specificMeal: MutableList<MealIngredients>){
        val mealDiffUtil = SearchDiffUtil(specificMealList, specificMeal)
        val mealDiffResult = DiffUtil.calculateDiff(mealDiffUtil)
        this.specificMealList = specificMeal
        mealDiffResult.dispatchUpdatesTo(this)
    }

}