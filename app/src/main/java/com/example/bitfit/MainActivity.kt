package com.example.bitfit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var nutritionRv : RecyclerView
    private lateinit var nutritionAdapter: NutritionAdapter
    private val nutrition = mutableListOf<Nutrition>()

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nutritionRv = findViewById(R.id.nutrition_logs)
        nutritionAdapter = NutritionAdapter(this, nutrition)
        nutritionRv.adapter = nutritionAdapter
        nutritionRv.layoutManager = LinearLayoutManager(this).also{
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            nutritionRv.addItemDecoration(dividerItemDecoration)
        }


        lifecycleScope.launch {
            (application as NutritionApplication).db.NutritionDAO().getALl()
                .collect { databaseList: List<NutritionEntity> ->
                    databaseList.map { entity ->
                        Nutrition(
                            entity.nutritionName,
                            entity.calorieCount
                        )
                    }.also {mappedList ->
                        nutrition.clear()
                        nutrition.addAll(mappedList)
                        nutritionAdapter.notifyDataSetChanged()
                    }
                }
        }

        val addNutrition = findViewById<Button>(R.id.newAddBtn)
        addNutrition.setOnClickListener{
            val intent = Intent(this, NutritionActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode == 1) {
            if (resultCode == RESULT_OK){
                val nutritionResult = data?.getSerializableExtra("result") as Nutrition
                nutrition.add(nutritionResult)
                nutritionAdapter.notifyDataSetChanged()

                lifecycleScope.launch(IO) {
                    (application as NutritionApplication).db.NutritionDAO().insert(
                        NutritionEntity(
                            nutritionName = nutritionResult.nutritionName,
                            calorieCount = nutritionResult.calorieCount
                        )
                    )
                }
            }
        }
    }
}