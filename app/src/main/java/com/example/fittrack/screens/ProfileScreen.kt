package com.example.fittrack.screens

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fittrack.AppViewModel
import com.example.fittrack.ScreenLayout
import com.example.fittrack.ui.Avatar
import com.example.fittrack.ui.GrayButton
import com.example.fittrack.ui.InputField
import com.example.fittrack.ui.MainButton
import com.example.fittrack.ui.MessageBox

@Composable
fun ProfileScreen(vm: AppViewModel, nav: NavController) {
    val user = vm.currentUser ?: return
    val context = LocalContext.current
    var name by remember { mutableStateOf(user.name) }
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val stream = context.contentResolver.openInputStream(uri)
            vm.avatar = BitmapFactory.decodeStream(stream)
            stream?.close()
        }
    }

    ScreenLayout("Профиль", "profile", vm, nav) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Avatar(vm.avatar, 110)
            Text(
                text = user.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text("@${user.login}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        GrayButton("Сменить аватар") {
            picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        InputField("Имя (отображаемое)", name, { name = it })
        InputField("Email", email, { email = it }, keyboardType = KeyboardType.Email)
        InputField("Новый пароль (оставьте пустым)", password, { password = it }, isPassword = true)

        if (message.isNotEmpty()) {
            MessageBox(message, message != "Профиль сохранён")
        }

        MainButton("Сохранить изменения", onClick = {
            message = vm.updateProfile(name.trim(), email.trim(), password)
            password = ""
        })
        MainButton("Выйти из аккаунта", color = MaterialTheme.colorScheme.error, onClick = {
            vm.logout()
            nav.navigate("login") {
                popUpTo(0)
            }
        })
    }
}
