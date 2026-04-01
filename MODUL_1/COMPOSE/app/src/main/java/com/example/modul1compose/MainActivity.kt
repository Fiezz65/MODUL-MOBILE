package com.example.modul1compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.modul1compose.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceRollerApp() {
    var leftDice by remember { mutableStateOf(1) }
    var rightDice by remember { mutableStateOf(1) }

    val context = LocalContext.current
    val msgDouble = stringResource(R.string.double_message)
    val msgLose = stringResource(R.string.bad_message)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DiceView(leftDice)
            Spacer(modifier = Modifier.width(16.dp))
            DiceView(rightDice)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            leftDice = (1..6).random()
            rightDice = (1..6).random()

            if (leftDice == rightDice) {
                Toast.makeText(context, msgDouble, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, msgLose, Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(stringResource(R.string.roll))
        }

    }
}

@Composable
fun DiceView(value: Int) {
    val imageRes = when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }

    Image(
        painter = painterResource(imageRes),
        contentDescription = value.toString()
    )
}