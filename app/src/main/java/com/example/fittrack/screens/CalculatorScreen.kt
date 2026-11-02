package com.example.fittrack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.ScreenLayout
import com.example.fittrack.data.Activity
import com.example.fittrack.ui.DateField
import com.example.fittrack.ui.InputField
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MessageBox
import com.example.fittrack.ui.WhiteCard
import com.example.fittrack.ui.formatDate
import com.example.fittrack.ui.formatNumber

@Composable
fun CalculatorScreen(vm: AppViewModel, nav: NavController) {
    var distance by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Activity?>(null) }
    var error by remember { mutableStateOf("") }

    ScreenLayout("Калькулятор", "home", vm, nav) {
        Column {
            Text(
                text = "Калькулятор активности",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Введите пройденное расстояние и время",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DateField("Дата", vm.selectedDate) { nav.navigate("date") }
        InputField("Пройдено километров", distance, { distance = it }, "Например: 5", keyboardType = KeyboardType.Decimal, suffix = "км")
        InputField("Время (минут)", minutes, { minutes = it }, "Например: 60", keyboardType = KeyboardType.Number, suffix = "мин")

        if (error.isNotEmpty()) {
            MessageBox(error, true)
        }

        MainButton("Рассчитать и сохранить", onClick = {
            val km = distance.replace(",", ".").toDoubleOrNull() ?: 0.0
            val min = minutes.toIntOrNull() ?: 0
            val activity = vm.addActivity(km, min)
            if (activity == null) {
                error = "Введите километры и время"
                result = null
            } else {
                error = ""
                result = activity
                distance = ""
                minutes = ""
            }
        })

        val item = result
        if (item != null) {
            Text(
                text = "Результат",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                ResultCard("Шаги", formatNumber(item.steps), "≈ 1312 шагов на 1 км", Modifier.weight(1f))
                ResultCard("Калории", item.calories.toString(), "≈ 7 ккал за 1 минуту", Modifier.weight(1f))
            }
            MessageBox(
                "Добавлено за ${formatDate(item.date)}: ${formatNumber(item.steps)} шагов, ${item.calories} ккал",
                false
            )
        }

        Text(
            text = "Шаги = км × 1312   ·   Калории = минуты × 7",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ResultCard(label: String, value: String, hint: String, modifier: Modifier) {
    WhiteCard(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 30.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
            Text(hint, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
