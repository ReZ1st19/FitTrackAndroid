package com.example.fittrack

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.fittrack.data.Activity
import com.example.fittrack.data.Stats
import com.example.fittrack.data.User
import java.time.DayOfWeek
import java.time.LocalDate

class AppViewModel : ViewModel() {

    private val users = mutableListOf<User>()
    val activities = mutableStateListOf<Activity>()

    var currentUser by mutableStateOf<User?>(null)
        private set
    var darkTheme by mutableStateOf(false)
    var selectedDate by mutableStateOf(LocalDate.now())
    var avatar by mutableStateOf<Bitmap?>(null)

    fun login(login: String, password: String): String? {
        if (login.isBlank() || password.isBlank()) return "Заполните все поля"
        val user = users.find { (it.login == login || it.email == login) && it.password == password }
            ?: return "Неверный логин или пароль"
        currentUser = user
        return null
    }

    fun register(login: String, password: String, email: String): String? {
        if (login.isBlank() || password.isBlank() || email.isBlank()) return "Заполните все поля"
        if (!email.contains("@")) return "Неверный email"
        if (password.length < 4) return "Пароль должен быть не короче 4 символов"
        if (users.any { it.login == login || it.email == email }) return "Такой пользователь уже есть"
        val user = User(login, password, email)
        users.add(user)
        currentUser = user
        return null
    }

    fun resetPassword(value: String): String {
        if (value.isBlank()) return "Введите email или логин"
        val user = users.find { it.login == value || it.email == value }
        return if (user == null) "Пользователь не найден" else "Инструкция отправлена на ${user.email}"
    }

    fun logout() {
        currentUser = null
        avatar = null
        selectedDate = LocalDate.now()
    }

    fun updateProfile(name: String, email: String, password: String): String {
        val user = currentUser ?: return "Ошибка"
        if (name.isBlank() || email.isBlank()) return "Имя и email не могут быть пустыми"
        if (!email.contains("@")) return "Неверный email"
        val newUser = user.copy(
            name = name,
            email = email,
            password = if (password.isBlank()) user.password else password
        )
        saveUser(newUser)
        return "Профиль сохранён"
    }

    fun updateGoals(steps: Int, distance: Double, calories: Int, time: Double) {
        val user = currentUser ?: return
        saveUser(user.copy(stepsGoal = steps, distanceGoal = distance, caloriesGoal = calories, timeGoal = time))
    }

    private fun saveUser(user: User) {
        val index = users.indexOfFirst { it.login == user.login }
        if (index != -1) users[index] = user
        currentUser = user
    }

    fun addActivity(distance: Double, minutes: Int): Activity? {
        val user = currentUser ?: return null
        if (distance <= 0 || minutes <= 0) return null
        val steps = (distance * 1312).toInt()
        val calories = minutes * 7
        val index = activities.indexOfFirst { it.login == user.login && it.date == selectedDate }
        if (index == -1) {
            activities.add(Activity(user.login, selectedDate, distance, minutes, steps, calories))
        } else {
            val old = activities[index]
            activities[index] = old.copy(
                distance = old.distance + distance,
                minutes = old.minutes + minutes,
                steps = old.steps + steps,
                calories = old.calories + calories
            )
        }
        return Activity(user.login, selectedDate, distance, minutes, steps, calories)
    }

    fun hasActivity(date: LocalDate): Boolean {
        return activities.any { it.login == currentUser?.login && it.date == date }
    }

    fun getStats(period: String): Stats {
        val login = currentUser?.login ?: return Stats()
        val list = activities.filter { it.login == login }.filter {
            when (period) {
                "week" -> {
                    val start = selectedDate.with(DayOfWeek.MONDAY)
                    !it.date.isBefore(start) && !it.date.isAfter(start.plusDays(6))
                }
                "month" -> it.date.year == selectedDate.year && it.date.month == selectedDate.month
                else -> it.date == selectedDate
            }
        }
        return Stats(
            steps = list.sumOf { it.steps },
            distance = list.sumOf { it.distance },
            minutes = list.sumOf { it.minutes },
            calories = list.sumOf { it.calories }
        )
    }

    fun daysInPeriod(period: String): Int {
        return when (period) {
            "week" -> 7
            "month" -> selectedDate.lengthOfMonth()
            else -> 1
        }
    }
}
