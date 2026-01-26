package com.watermuffin.todaylist

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.watermuffin.todaylist.ui.navigation.BottomNavBar
import com.watermuffin.todaylist.ui.screens.calendar.CalendarScreen
import com.watermuffin.todaylist.ui.screens.notes.NotesScreen
import com.watermuffin.todaylist.ui.screens.profile.ProfileScreen
import com.watermuffin.todaylist.ui.screens.statistics.StatisticsScreen
import com.watermuffin.todaylist.ui.screens.todays.TodaysScreen
import com.watermuffin.todaylist.ui.theme.TodayListTheme
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watermuffin.todaylist.data.database.TodayListDatabase
import com.watermuffin.todaylist.ui.screens.auth.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database: TodayListDatabase = TodayListDatabase.getDatabase(this)

        enableEdgeToEdge()
        setContent {
            TodayListTheme {
                AppEntryPoint()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)

@Composable
fun AppEntryPoint() {
    val viewModel: UserViewModel = viewModel()
    val users by viewModel.users.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()

    when {
        users.isEmpty() -> AuthScreen(viewModel)
//        activeUser == null -> UserSelectionScreen(viewModel)
        else -> MainScreen(viewModel)
    }
}

@Composable
fun UserSelectionScreen(viewModel: UserViewModel) {
    val scope = rememberCoroutineScope()
}

@Composable
fun AuthScreen(viewModel: UserViewModel) {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            val userId = viewModel.createUser("WaterMusffin", null)
            Log.d("CUSTOM DEBUG", "CREATED USER $userId")
        }
    }) {
        Text("Создать профиль")
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(viewModel: UserViewModel) {
    var currentScreen by remember { mutableStateOf("todays") }
    val database: TodayListDatabase = TodayListDatabase.getDatabase(LocalContext.current)

    LaunchedEffect(Unit) {
        if (viewModel.activeUser.value?.id == null) {
            if (viewModel.getUser(1).value == null) Log.e("USER ERROR", "There are no users...")
            else viewModel.selectUser(1)
        } else {
            Log.d("APP STARTING", "ACTIVE USER: ${viewModel.activeUser.value?.id}")
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentScreen,
                onNavigate = { route ->
                    currentScreen = route
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with
                            fadeOut(animationSpec = tween(300))
                },
                label = "screenAnimation"
            ) { screen ->
                when (screen) {
                    "statistics" -> StatisticsScreen()
                    "calendar" -> CalendarScreen()
                    "todays" -> TodaysScreen()
                    "notes" -> NotesScreen()
                    "profile" -> ProfileScreen()
                }
            }
        }
    }
}
