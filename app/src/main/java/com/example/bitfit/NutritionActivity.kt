package com.example.bitfit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class NutritionActivity : AppCompatActivity() {
    private lateinit var nutritionRv: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nutrition_details)
        val nutritionInput : EditText = findViewById(R.id.food_entry)
        val calorieCountInput : EditText = findViewById(R.id.calorie_entry)
        val addInput : Button = findViewById(R.id.nutritionBtn)

        addInput.setOnClickListener(View.OnClickListener {

            val intent = Intent()
            intent.putExtra("result", Nutrition(nutritionName = nutritionInput.text.toString(),
                calorieCount = calorieCountInput.text.toString()))
            setResult(RESULT_OK, intent)
            finish()
        })
    }
}