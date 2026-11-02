package com.example.fittrack.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.ScreenLayout
import com.example.fittrack.ui.GrayButton
import com.example.fittrack.ui.InputField
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MessageBox

@Composable
fun SettingsScreen(vm: AppViewModel, nav: NavController) {
    val user = vm.currentUser ?: return
    var steps by remember { mutableStateOf(user.stepsGoal.toString()) }
    var distance by remember { mutableStateOf(user.distanceGoal.toString()) }
    var calories by remember { mutableStateOf(user.caloriesGoal.toString()) }
    var time by remember { mutableStateOf(user.timeGoal.toString()) }
    var message by remember { mutableStateOf("") }

    ScreenLayout("Настройки", "settings", vm, nav) {
        Text(
            text = "Тема оформления",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            ThemeButton("Светлая", !vm.darkTheme, Modifier.weight(1f)) { vm.darkTheme = false }
            ThemeButton("Темная", vm.darkTheme, Modifier.weight(1f)) { vm.darkTheme = true }
        }

        Text(
            text = "Цели (Сегодня)",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        InputField("Шаги (цель)", steps, { steps = it }, keyboardType = KeyboardType.Number)
        InputField("Дистанция (км)", distance, { distance = it }, keyboardType = KeyboardType.Decimal)
        InputField("Калории (Kcal)", calories, { calories = it }, keyboardType = KeyboardType.Number)
        InputField("Время активности (ч)", time, { time = it }, keyboardType = KeyboardType.Decimal)

        if (message.isNotEmpty()) {
            MessageBox(message, message != "Цели сохранены")
        }

        MainButton("Сохранить", onClick = {
            val s = steps.toIntOrNull()
            val d = distance.replace(",", ".").toDoubleOrNull()
            val c = calories.toIntOrNull()
            val t = time.replace(",", ".").toDoubleOrNull()
            if (s == null || d == null || c == null || t == null || s <= 0 || d <= 0 || c <= 0 || t <= 0) {
                message = "Введите корректные значения"
            } else {
                vm.updateGoals(s, d, c, t)
                message = "Цели сохранены"
            }
        })
        GrayButton("Отмена") {
            steps = user.stepsGoal.toString()
            distance = user.distanceGoal.toString()
            calories = user.caloriesGoal.toString()
            time = user.timeGoal.toString()
            message = ""
        }
    }
}

@Composable
fun ThemeButton(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    val color = MaterialTheme.colorScheme.primary
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) color else MaterialTheme.colorScheme.outline
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = if (selected) color.copy(alpha = 0.12f) else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier.height(48.dp)
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
