package com.example.fittrack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.ui.InputField
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MessageBox

@Composable
fun RegisterScreen(vm: AppViewModel, nav: NavController) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Регистрация",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (error.isNotEmpty()) {
            MessageBox(error, true)
        }

        InputField("Логин", login, { login = it }, "Придумайте логин")
        InputField("Пароль", password, { password = it }, "Придумайте пароль", isPassword = true)
        InputField("Email", email, { email = it }, "example@gmail.com", keyboardType = KeyboardType.Email)

        MainButton("Зарегистрироваться", onClick = {
            val result = vm.register(login.trim(), password, email.trim())
            if (result == null) {
                nav.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                error = result
            }
        })

        TextButton(
            onClick = { nav.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Уже есть аккаунт? Войти", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
