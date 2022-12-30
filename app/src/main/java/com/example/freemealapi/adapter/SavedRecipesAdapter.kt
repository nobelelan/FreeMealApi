package com.example.freemealapi.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.freemealapi.R
import com.example.freemealapi.databinding.RvSavedRecipesBinding
import com.example.freemealapi.models.MealIngredients
import com.example.freemealapi.ui.YoutubeWebActivity

class SavedRecipesAdapter: RecyclerView.Adapter<SavedRecipesAdapter.SavedRecipesViewHolder>() {

    var watchVideoClickListener: OnWatchVideoClickListener? = null

    interface OnWatchVideoClickListener{
        fun onItemClick(strYoutube: String)
    }

    fun setOnClickListener(listener: OnWatchVideoClickListener){
        watchVideoClickListener = listener
    }

    inner class SavedRecipesViewHolder(val binding: RvSavedRecipesBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<MealIngredients>(){
        override fun areItemsTheSame(oldItem: MealIngredients, newItem: MealIngredients): Boolean {
            return oldItem.strMealThumb == newItem.strMealThumb
        }

        override fun areContentsTheSame(
            oldItem: MealIngredients,
            newItem: MealIngredients
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedRecipesViewHolder {
        return SavedRecipesViewHolder(RvSavedRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SavedRecipesViewHolder, position: Int) {

        val ingredients = differ.currentList[position]

        Glide.with(holder.itemView.context)
            .load(ingredients.strMealThumb)
            .transition(DrawableTransitionOptions.withCrossFade())
            .error(R.drawable.default_food_img)
            .into(holder.binding.imgMealImage)

        holder.binding.txtYtWatch.setOnClickListener {
            watchVideoClickListener!!.onItemClick(ingredients.strYoutube!!)
        }

        holder.binding.txtMealName.text = ingredients.strMeal
        holder.binding.txtAreaName.text = ingredients.strArea
        holder.binding.txtCategory.text = ingredients.strCategory

        holder.binding.txtInstructions.text = ingredients.strInstructions

        holder.binding.txtIngredients.text = "${ingredients.strIngredient1}       ${ingredients.strMeasure1}\n" +
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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}