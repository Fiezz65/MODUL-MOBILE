package com.example.modul1xml

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val diceImage1: ImageView = findViewById(R.id.diceImage1)
        val diceImage2: ImageView = findViewById(R.id.diceImage2)
        val rollButton: Button = findViewById(R.id.rollButton)

        diceImage1.setImageResource(R.drawable.dice_1)
        diceImage2.setImageResource(R.drawable.dice_1)

        rollButton.setOnClickListener {
            val result1 = (1..6).random()
            val result2 = (1..6).random()

            diceImage1.setImageResource(getDiceImage(result1))
            diceImage2.setImageResource(getDiceImage(result2))

            if (result1 == result2) {
                val msgDouble = getString(R.string.double_message)
                Toast.makeText(this, msgDouble, Toast.LENGTH_SHORT).show()
            } else {
                val msgLose = getString(R.string.bad_message)
                Toast.makeText(this, msgLose, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDiceImage(diceValue: Int): Int {
        return when (diceValue) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
    }
}