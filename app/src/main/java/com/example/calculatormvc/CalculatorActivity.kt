package com.example.calculatormvc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
 * CalculatorActivity acts as a Controller between CalculatorModel and activity_main (Views)
 */
class CalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Calculator(this)
    }
}