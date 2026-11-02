package com.example.fittrack.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.R
import com.example.fittrack.ui.InputField
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MessageBox

@Composable
fun LoginScreen(vm: AppViewModel, nav: NavController) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Icon(
            painter = painterResource(R.drawable.ic_walk),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = "FitTrack",
            fontSize = 32.sp,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Мониторинг физической активности",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (error.isNotEmpty()) {
            MessageBox(error, true)
        }

        InputField("Логин", login, { login = it }, "Введите логин")
        InputField("Пароль", password, { password = it }, "Введите пароль", isPassword = true)

        MainButton("Войти", onClick = {
            val result = vm.login(login.trim(), password)
            if (result == null) {
                nav.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                error = result
            }
        })

        Row(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { nav.navigate("register") }) {
                Text("Регистрация", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { nav.navigate("reset") }) {
                Text("Забыли пароль?", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
