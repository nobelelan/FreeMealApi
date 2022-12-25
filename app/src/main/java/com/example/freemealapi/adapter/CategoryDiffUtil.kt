package com.example.freemealapi.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.freemealapi.models.Category

class CategoryDiffUtil(
    private val oldList: MutableList<Category>,
    private val newList: MutableList<Category>
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
        return oldList[oldItemPosition].idCategory == newList[newItemPosition].idCategory &&
                oldList[oldItemPosition].strCategory == newList[newItemPosition].strCategory &&
                oldList[oldItemPosition].strCategoryDescription == newList[newItemPosition].strCategoryDescription &&
                oldList[oldItemPosition].strCategoryThumb == newList[newItemPosition].strCategoryThumb
    }
}