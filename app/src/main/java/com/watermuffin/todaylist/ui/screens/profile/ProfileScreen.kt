package com.watermuffin.todaylist.ui.screens.profile

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watermuffin.todaylist.ui.screens.auth.UserViewModel
import com.watermuffin.todaylist.ui.screens.profile.components.ProfileAvatarImage
import com.watermuffin.todaylist.ui.screens.profile.helpers.saveAvatarToStorage
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.PrimitiveKind

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val viewModel: UserViewModel = viewModel()
    val activeUser by viewModel.activeUser.collectAsState()
    var avatarPath by remember { mutableStateOf<String?>(activeUser?.avatarFileName) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Профиль",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(50.dp))

        ProfileAvatarImage(avatarPath = avatarPath) { uri ->
            val savedFileName = saveAvatarToStorage(context, uri)

            if (savedFileName != null) {
                avatarPath = savedFileName
                scope.launch {
                    activeUser.let {
                        val userId = activeUser?.id ?: 0
                        viewModel.updateUserAvatar(userId, savedFileName)
                    }
                }
            }
            else {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}