package com.example.fittrack.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.fittrack.ui.DateField
import com.example.fittrack.ui.GrayButton
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.WhiteCard
import java.time.LocalDate
import java.time.YearMonth

val monthNames = listOf(
    "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
    "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
)
val weekDays = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "ВС")

@Composable
fun DateScreen(vm: AppViewModel, nav: NavController) {
    var picked by remember { mutableStateOf(vm.selectedDate) }
    var month by remember { mutableStateOf(YearMonth.from(vm.selectedDate)) }

    val firstDay = month.atDay(1)
    val offset = firstDay.dayOfWeek.value - 1
    val rows = (offset + month.lengthOfMonth() + 6) / 7
    val start = firstDay.minusDays(offset.toLong())

    ScreenLayout("Выбор даты", "home", vm, nav) {
        DateField("Выбранная дата", picked, active = true) {}

        WhiteCard {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { month = month.minusMonths(1) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Назад")
                    }
                    Text(
                        text = "${monthNames[month.monthValue - 1]} ${month.year}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { month = month.plusMonths(1) }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Вперёд")
                    }
                }

                Row(modifier = Modifier.padding(vertical = 8.dp)) {
                    for (day in weekDays) {
                        Text(
                            text = day,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                for (row in 0 until rows) {
                    Row {
                        for (col in 0 until 7) {
                            val date = start.plusDays((row * 7 + col).toLong())
                            DayCell(
                                date = date,
                                inMonth = date.month == month.month,
                                selected = date == picked,
                                weekend = col >= 5,
                                hasActivity = vm.hasActivity(date),
                                modifier = Modifier.weight(1f)
                            ) {
                                picked = date
                                month = YearMonth.from(date)
                            }
                        }
                    }
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = " — есть активность за день",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        MainButton("Выбрать", onClick = {
            vm.selectedDate = picked
            nav.popBackStack()
        })
        GrayButton("Отмена") { nav.popBackStack() }
    }
}

@Composable
fun DayCell(
    date: LocalDate,
    inMonth: Boolean,
    selected: Boolean,
    weekend: Boolean,
    hasActivity: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val textColor = when {
        selected -> Color.White
        !inMonth -> MaterialTheme.colorScheme.outline
        weekend -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                fontSize = 15.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )
        }
        if (hasActivity && inMonth && !selected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp).width(4.dp))
        }
    }
}
