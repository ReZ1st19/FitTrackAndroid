package com.example.fittrack.data

import java.time.LocalDate

data class User(
    val login: String,
    val password: String,
    val email: String,
    val name: String = login,
    val stepsGoal: Int = 10000,
    val distanceGoal: Double = 10.0,
    val caloriesGoal: Int = 500,
    val timeGoal: Double = 2.0
)

data class Activity(
    val login: String,
    val date: LocalDate,
    val distance: Double,
    val minutes: Int,
    val steps: Int,
    val calories: Int
)

data class Stats(
    val steps: Int = 0,
    val distance: Double = 0.0,
    val minutes: Int = 0,
    val calories: Int = 0
)
