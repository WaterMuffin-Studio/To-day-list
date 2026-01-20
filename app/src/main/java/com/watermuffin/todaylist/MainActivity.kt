package com.watermuffin.todaylist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import com.watermuffin.todaylist.ui.theme.TodayListTheme

import java.nio.file.WatchEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodayListTheme {
                Scaffold() { innerPadding: PaddingValues ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        Greeting(
                            name = "WaterMuffin",
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        ButtonsBlock()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Привет, $name!",
            fontSize = 28.sp,
        )
        Text(
            text = "Количество твоих задач : ${cnt.value}",
            fontSize = 14.sp,
        )
    }
}

var cnt: MutableState<Int> = mutableStateOf(0)
@Composable
fun ButtonsBlock() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val buttons_context = LocalContext.current

        Button(
            onClick = {
                cnt.value += 1
//                val message = "Задача назначена, WaterMuffin! У вас ${cnt.value} задач"
//                Toast.makeText(buttons_context, message, Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Text(
                text = "Назначить задачу",
            )
        }
        Button(
            onClick = {
                cnt.value -= 1
//                val message = "Задача снята, WaterMuffin! У вас ${cnt.value} задач"
//                Toast.makeText(buttons_context, message, Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Text(
                text = "Снять задачу",
            )
        }
        Button(
            onClick = {
                val message = "На данный момент у вас ${cnt.value} задач"
                Toast.makeText(buttons_context, message, Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onBackground,
                contentColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Text(
                text = "Проверить ",
            )
        }
    }
}