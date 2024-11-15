package com.example.bitfit


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionDAO {
    @Query("Select * FROM nutrition_table")
    fun getALl(): Flow<List<NutritionEntity>>

    @Insert
    fun insert(nutrition: NutritionEntity)
}