package ru.skillbranch.calculator_without_library

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.result


class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_result.setOnClickListener {
            val oper = enter.text.toString()
          //  if (cheak(oper)) {
                val calc = StringCalculator()
                result.text = calc.calculate(oper)
           // } else result.text = "Неправильно введено выражение"
        }
    }




        fun cheak(str: String): Boolean {
            var ch = 0
            for (i in 0..str.length) {
                if (str[i] == '.' && ch == 0) {
                    ch++
                } else if (str[i] == '.' && ch != 0)
                    return false
                else if (str[i] == '+' || str[i] == '-' || str[i] == '*' || str[i] == '/') {
                    if (str[i + 1] == '+' || str[i + 1] == '-' || str[i + 1] == '*' || str[i + 1] == '/' || str[i - 1] == '+' || str[i - 1] == '-' || str[i - 1] == '*' || str[i - 1] == '/')
                        return false
                }
            }

            return true
        }

    }


