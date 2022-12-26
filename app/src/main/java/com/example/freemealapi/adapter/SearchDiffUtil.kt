package com.example.freemealapi.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.freemealapi.models.Meal
import com.example.freemealapi.models.MealIngredients

class SearchDiffUtil(
    private val oldList: MutableList<MealIngredients>,
    private val newList: MutableList<MealIngredients>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idMeal == newList[newItemPosition].idMeal &&
                oldList[oldItemPosition].strMeal == newList[newItemPosition].strMeal &&
                oldList[oldItemPosition].strMealThumb == newList[newItemPosition].strMealThumb
    }
}