package com.example.freemealapi.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.freemealapi.models.MealIngredients

@Database(entities = [MealIngredients::class], version = 1)
abstract class MealIngredientsDatabase: RoomDatabase() {

    abstract fun mealIngredientsDao(): MealIngredientsDao

    companion object{
        @Volatile
        private var INSTANCE: MealIngredientsDatabase? = null

        fun getDatabase(context: Context): MealIngredientsDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealIngredientsDatabase::class.java,
                    "meal_ingredients_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}