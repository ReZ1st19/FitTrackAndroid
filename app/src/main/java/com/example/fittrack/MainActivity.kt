package com.example.fittrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fittrack.screens.CalculatorScreen
import com.example.fittrack.screens.DateScreen
import com.example.fittrack.screens.HomeScreen
import com.example.fittrack.screens.LoginScreen
import com.example.fittrack.screens.ProfileScreen
import com.example.fittrack.screens.RegisterScreen
import com.example.fittrack.screens.ResetPasswordScreen
import com.example.fittrack.screens.SettingsScreen
import com.example.fittrack.ui.BottomBar
import com.example.fittrack.ui.TopBar
import com.example.fittrack.ui.theme.FitTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: AppViewModel = viewModel()
            FitTrackTheme(darkTheme = vm.darkTheme) {
                FitTrackApp(vm)
            }
        }
    }
}

@Composable
fun FitTrackApp(vm: AppViewModel) {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = "login",
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        composable("login") { LoginScreen(vm, nav) }
        composable("register") { RegisterScreen(vm, nav) }
        composable("reset") { ResetPasswordScreen(vm, nav) }
        composable("home") { HomeScreen(vm, nav) }
        composable("calculator") { CalculatorScreen(vm, nav) }
        composable("date") { DateScreen(vm, nav) }
        composable("profile") { ProfileScreen(vm, nav) }
        composable("settings") { SettingsScreen(vm, nav) }
    }
}

@Composable
fun ScreenLayout(
    title: String,
    tab: String,
    vm: AppViewModel,
    nav: NavController,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        TopBar(title, vm.avatar) { nav.navigate("profile") { launchSingleTop = true } }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            content = content
        )
        BottomBar(tab) { route ->
            nav.navigate(route) {
                popUpTo("home")
                launchSingleTop = true
            }
        }
    }
}
