package com.watermuffin.todaylist.ui.auth

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watermuffin.todaylist.MainActivity
import com.watermuffin.todaylist.R
import com.watermuffin.todaylist.data.database.TodayListDatabase
import com.watermuffin.todaylist.ui.screens.auth.UserViewModel
import com.watermuffin.todaylist.ui.screens.profile.components.ProfileAvatarImage
import com.watermuffin.todaylist.ui.screens.profile.helpers.saveAvatarToStorage
import com.watermuffin.todaylist.ui.theme.TodayListTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isFirstAuth = intent.getStringExtra("IS_FIRST_AUTH") ?: ""
        enableEdgeToEdge()

        setContent {
            TodayListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: UserViewModel = viewModel()
                    val scope = rememberCoroutineScope()

                    AuthScreen(
                        isFirstAuth = isFirstAuth,
                        onAuthComplete = {isComplete, nickname, avatar ->
                            if (isComplete) {
                                scope.launch {
                                    val userId: Long = viewModel.createUser(name = nickname, avatarFileName = avatar)
                                    viewModel.selectUser(userId)
                                    returnToMain(true)
                                }
                            }
                            else {
                                returnToMain()
                            }
                        }
                    )
                }
            }
        }
    }

    private fun returnToMain(isSlideBack: Boolean = false) {
        val intent = Intent(this, MainActivity::class.java).apply {

            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

            putExtra("STARTWITH", "profile")
        }

        val options = if (isSlideBack) {
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        } else {
            ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }


        startActivity(intent, options.toBundle())

        finish()
    }

    @Composable
    fun AuthScreen(
        isFirstAuth: String,
        onAuthComplete: (isComplete: Boolean, nickname: String, avatar: String) -> Unit
    ) {
        val context = LocalContext.current
        val focusManager = LocalFocusManager.current
        var inputText by remember { mutableStateOf("") }
        var avatarPath by remember { mutableStateOf<String?>(null) }
        var errorMessage by remember { mutableStateOf<String>("") }
        var isInputError by remember { mutableStateOf(false) }

        Column (
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProfileAvatarImage(
                avatarPath = avatarPath,
            ) { uri ->
                val savedFileName = saveAvatarToStorage(context, uri)

                if (savedFileName != null) {
                    avatarPath = savedFileName
                }
                else {
                    Toast.makeText(context, "IMAGE error", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { newText ->
                    if (newText.length <= 16) {
                        inputText = newText

                        if (newText.isNotEmpty()) {
                            isInputError = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(60.dp),
                label = { Text(stringResource(R.string.username_input_placeholder)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp),
                isError = isInputError
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isFirstAuth.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .clickable() {
                                onAuthComplete(false, "", "")
                            }
                            .fillMaxHeight()
                            .padding(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chevron_left),
                            tint = MaterialTheme.colorScheme.onBackground,
                            contentDescription = "back button"
                        )

                        Text (
                            text = stringResource(R.string.back),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                }
                else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                val nameError = stringResource(R.string.create_profile_error_name)
                val avatarError = stringResource(R.string.create_profile_error_avatar)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(12.dp))
                        .clickable() {
                            if (inputText.trim() == "") {
                                errorMessage = nameError
                                isInputError = true
                            }
                            else if (avatarPath == null) {
                                errorMessage = avatarError
                                isInputError = false
                            }
                            else {
                                onAuthComplete(true, inputText.trim(), avatarPath ?: "")
                            }
                        }
                        .fillMaxHeight()
                        .padding(6.dp)
                ) {
                    Text (
                        text = stringResource(R.string.create_button),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 10.dp)
                    )

                    Icon(
                        painter = painterResource(R.drawable.chevron_right),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "back button"
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        }
    }
}