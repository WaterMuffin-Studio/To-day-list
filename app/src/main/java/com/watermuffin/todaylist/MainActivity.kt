package com.watermuffin.todaylist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import androidx.compose.animation.togetherWith
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watermuffin.todaylist.data.database.TodayListDatabase
import com.watermuffin.todaylist.ui.auth.AuthActivity
import com.watermuffin.todaylist.ui.screens.auth.UserViewModel
import com.watermuffin.todaylist.ui.screens.profile.components.changeAppLanguage
import com.watermuffin.todaylist.ui.screens.profile.goToAuth
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val app = application as TodayListApplication
        val startScreen = intent.getStringExtra("STARTWITH") ?: "todays"
        val database: TodayListDatabase = TodayListDatabase.getDatabase(this)

        lifecycleScope.launch {
            val savedLang = app.languageManager.getSavedLanguage()
            changeAppLanguage(savedLang)

            enableEdgeToEdge()
            setContent {
                TodayListTheme {
                    AppEntryPoint(startScreen)
                }
            }
        }

        super.onCreate(savedInstanceState)
    }
}

@OptIn(ExperimentalAnimationApi::class)

@Composable
fun AppEntryPoint(mainStartScreen: String) {
    val viewModel: UserViewModel = viewModel()
    val users by viewModel.users.collectAsState()
    val activeUser by viewModel.activeUser.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(users) {
        if (users != null && users?.isEmpty() == true) {
            goToAuth(context = context, clearStack = true, isFirst = true)
        }
    }

    MainScreen(viewModel, mainStartScreen)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(viewModel: UserViewModel, startScreen: String) {
    var currentScreen by remember { mutableStateOf(startScreen) }
    val database: TodayListDatabase = TodayListDatabase.getDatabase(LocalContext.current)

//    LaunchedEffect(viewModel.activeUser) {
//        Log.d("WHAT USERS", "USERS ${viewModel.users.value}")
//        if (viewModel.activeUser.value?.id == null) {
//            viewModel.selectUser(1)
//        } else {
//            Log.d("APP STARTING", "ACTIVE USER: ${viewModel.activeUser.value?.id}")
//        }
//    }

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
                    EnterTransition.None togetherWith ExitTransition.None
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
