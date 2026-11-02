package com.example.fittrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.ScreenLayout
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MetricCard
import com.example.fittrack.ui.WhiteCard
import com.example.fittrack.ui.formatDate
import com.example.fittrack.ui.formatDouble
import com.example.fittrack.ui.formatNumber

@Composable
fun HomeScreen(vm: AppViewModel, nav: NavController) {
    var period by rememberSaveable { mutableStateOf("day") }
    val user = vm.currentUser ?: return
    val stats = vm.getStats(period)
    val days = vm.daysInPeriod(period)

    val stepsGoal = user.stepsGoal * days
    val distanceGoal = user.distanceGoal * days
    val caloriesGoal = user.caloriesGoal * days
    val timeGoal = user.timeGoal * days
    val hours = stats.minutes / 60.0

    val stepsProgress = stats.steps.toFloat() / stepsGoal
    val distanceProgress = (stats.distance / distanceGoal).toFloat()
    val caloriesProgress = stats.calories.toFloat() / caloriesGoal
    val timeProgress = (hours / timeGoal).toFloat()
    val total = (stepsProgress.coerceAtMost(1f) + distanceProgress.coerceAtMost(1f) +
            caloriesProgress.coerceAtMost(1f) + timeProgress.coerceAtMost(1f)) / 4 * 100

    ScreenLayout("FitTrack", "home", vm, nav) {
        PeriodTabs(period) { period = it }

        WhiteCard(modifier = Modifier.clickable { nav.navigate("date") }) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Дата: ${formatDate(vm.selectedDate)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (period == "day") {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("${formatDouble(hours)} ч", fontSize = 36.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("Общее время активности", fontSize = 16.sp, color = Color.White)
                Text(
                    text = "Общий прогресс: ${total.toInt()}%",
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            MetricCard("Шаги", formatNumber(stats.steps), "${formatNumber(stats.steps)} / ${formatNumber(stepsGoal)}", stepsProgress)
            MetricCard("Дистанция", "${formatDouble(stats.distance)} км", "${formatDouble(stats.distance)} / ${formatDouble(distanceGoal)} км", distanceProgress)
            MetricCard("Калории", "${stats.calories} Kcal", "${stats.calories} / $caloriesGoal Kcal", caloriesProgress)
            MetricCard("Время", "${formatDouble(hours)} ч", "${formatDouble(hours)} / ${formatDouble(timeGoal)} ч", timeProgress)
        } else {
            WhiteCard {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    StatRow("Шаги", formatNumber(stats.steps))
                    HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    StatRow("Дистанция", "${formatDouble(stats.distance)} км")
                    HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    StatRow("Время активности", "${formatDouble(hours)} ч")
                    HorizontalDivider(color = MaterialTheme.colorScheme.background)
                    StatRow("Калории", "${formatNumber(stats.calories)} Kcal")
                }
            }
        }

        MainButton("Добавить активность", onClick = { nav.navigate("calculator") })
    }
}

@Composable
fun PeriodTabs(selected: String, onSelect: (String) -> Unit) {
    WhiteCard {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            TabItem("День", selected == "day", Modifier.weight(1f)) { onSelect("day") }
            TabItem("Неделя", selected == "week", Modifier.weight(1f)) { onSelect("week") }
            TabItem("Месяц", selected == "month", Modifier.weight(1f)) { onSelect("month") }
        }
    }
}

@Composable
fun TabItem(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    )
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
