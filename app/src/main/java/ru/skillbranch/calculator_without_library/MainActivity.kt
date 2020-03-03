package ru.skillbranch.calculator_without_library

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.result


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_result.setOnClickListener {
            val oper = enter.text.toString()
            val calc = StringCalculator()

            result.text = calc.calculate(operation = oper)
        }
    }
}

