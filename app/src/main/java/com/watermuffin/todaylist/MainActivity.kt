package com.watermuffin.todaylist

import android.os.Bundle
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
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TodayListTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf("todays") }

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
