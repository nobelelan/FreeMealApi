package com.example.freemealapi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.freemealapi.R
import com.example.freemealapi.databinding.RvMainCategoryItemBinding
import com.example.freemealapi.models.Category

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var categoryList = mutableListOf<Category>()
    var categoryItemListener: OnItemClickListener? = null

    inner class CategoryViewHolder(val binding: RvMainCategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener{
        fun onClicked(categoryName: String)
    }
    fun setClickListener(listener1: OnItemClickListener){
        categoryItemListener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(RvMainCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(categoryList[position].strCategoryThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_food_img)
            .into(holder.binding.imgDishImage)

        holder.binding.txtDishType.text = categoryList[position].strCategory
        holder.binding.txtDishDescription.text = categoryList[position].strCategoryDescription

        holder.itemView.rootView.setOnClickListener {
            categoryItemListener!!.onClicked(categoryList[position].strCategory)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategoryData(category: MutableList<Category>){
        val categoryDiffUtil = CategoryDiffUtil(categoryList, category)
        val categoryDiffResult = DiffUtil.calculateDiff(categoryDiffUtil)
        this.categoryList = category
        categoryDiffResult.dispatchUpdatesTo(this)
    }
}