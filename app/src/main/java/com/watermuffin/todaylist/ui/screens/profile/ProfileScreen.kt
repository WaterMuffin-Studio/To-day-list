package com.watermuffin.todaylist.ui.screens.profile

import android.app.ActivityOptions
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.watermuffin.todaylist.ui.screens.auth.UserViewModel
import com.watermuffin.todaylist.ui.screens.profile.components.ProfileAvatarImage
import com.watermuffin.todaylist.ui.screens.profile.components.ProfileMenuButton
import com.watermuffin.todaylist.ui.screens.profile.helpers.saveAvatarToStorage
import kotlinx.coroutines.launch
import com.watermuffin.todaylist.R
import com.watermuffin.todaylist.TodayListApplication
import com.watermuffin.todaylist.data.store.AppSettings
import com.watermuffin.todaylist.ui.auth.AuthActivity
import com.watermuffin.todaylist.ui.screens.profile.components.ProfileMenuButtonSpacer
import com.watermuffin.todaylist.ui.common.ReusableDialog
import com.watermuffin.todaylist.ui.screens.profile.components.SelectUserButton
import com.watermuffin.todaylist.ui.screens.profile.components.changeAppLanguage

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as TodayListApplication
    val settings = remember { app.languageManager }

    val currentLang by settings.languageFlow.collectAsState(initial = "ru")

    val viewModel: UserViewModel = viewModel()
    val activeUser by viewModel.activeUser.collectAsState(initial = null)
    var avatarPath by remember { mutableStateOf<String?>(activeUser?.avatarFileName) }
    var username by remember { mutableStateOf(activeUser?.name) }
    val allUsers by viewModel.users.collectAsState()
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState()
    var showSelectUserDialog by remember { mutableStateOf<Boolean>(false) }
    var showEditUsernameDialog by remember { mutableStateOf<Boolean>(false) }
    var showChangeLanguageDialog by remember { mutableStateOf<Boolean>(false) }
    var isUsernameInputError by remember { mutableStateOf<Boolean>(false) }

    LaunchedEffect(activeUser) {
        if (activeUser != null) {
            avatarPath = activeUser?.avatarFileName
            username = activeUser?.name
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        ProfileAvatarImage(avatarPath = activeUser?.avatarFileName) { uri ->
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

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(shape = RoundedCornerShape(4.dp))
                .clickable(
                    onClick = {
                        showEditUsernameDialog = true
                    },
                    interactionSource = interactionSource,
                    indication = ripple(
                        bounded = true,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                )
                .padding(horizontal = 5.dp, vertical = 1.dp)
        ) {
            Text(
                text = activeUser?.name ?: "Guest",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.width(5.dp))

            Icon(
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = "button icon",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        ProfileMenuButtonSpacer()
        ProfileMenuButton(
            icon = R.drawable.account_reactivate_outline,
            title = stringResource(R.string.profile_menu_button_select_profile)
        ) {
            showSelectUserDialog = true
        }
        ProfileMenuButtonSpacer()

        ProfileMenuButton(
            icon = R.drawable.cog_outline,
            title = stringResource(R.string.profile_menu_button_settings)
        ) {

        }

        ProfileMenuButton(
            icon = R.drawable.web,
            title = stringResource(R.string.profile_menu_button_language)
        ) {
            showChangeLanguageDialog = true
        }

        ProfileMenuButton(
            icon = R.drawable.information_outline,
            title = stringResource(R.string.profile_menu_button_about)
        ) {

        }
    }


    ReusableDialog(
        isVisible = showSelectUserDialog,
        onDismiss = { showSelectUserDialog = false }
    ) {
        val windowInfo = LocalWindowInfo.current
        val containerSize = windowInfo.containerSize
        val density = LocalDensity.current
        val maxListHeightDp = with(density) { (containerSize.height * 0.5f).toDp() }

        val usersList = allUsers
        if (usersList != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp, max = maxListHeightDp),
                contentPadding = PaddingValues(0.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(usersList) { user ->
                    SelectUserButton (
                        username = user.name,
                        avatarFileName = user.avatarFileName,
                        onClick = {
                            val targetUser = viewModel.getUser(user.id).value
                            if (activeUser?.id != targetUser?.id) {
                                username = user.name
                                avatarPath = user.avatarFileName
                                scope.launch {
                                    viewModel.selectUser(user.id)
                                }
                            }
//                            else {
//                                if (username != targetUser?.name) username = user.name
//                                if (avatarPath != targetUser?.name) avatarPath = user.avatarFileName
//                            }
                            showSelectUserDialog = false
                        }
                    )
                }
            }
        }


        ProfileMenuButtonSpacer()

        SelectUserButton (
            username = stringResource(R.string.create_user),
            avatarFileName = null,
            onClick = {
                goToAuth(context, false)
                showSelectUserDialog = false
            },
            size = 60,
            iconPath = R.drawable.plus,
        )
    }

    ReusableDialog(
        isVisible = showEditUsernameDialog,
        onDismiss = {
            showEditUsernameDialog = false
            isUsernameInputError = false
        }
    ) {
        val namePlaceholder: String = activeUser?.name ?: ""
        var inputText by remember { mutableStateOf(namePlaceholder) }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            TextField(
                value = inputText,
                onValueChange = { newText ->
                    if (newText.length <= 16) {
                        inputText = newText
                        if (newText.isNotEmpty()) {
                            isUsernameInputError = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.75f),
                label = { Text(stringResource(R.string.username_input_placeholder)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                    errorTextColor = MaterialTheme.colorScheme.error,
                ),
                shape = RoundedCornerShape(12.dp),
                isError = isUsernameInputError,
            )
            Spacer(modifier = Modifier.width(3.dp))

            Icon(
                painter = painterResource(R.drawable.check),
                tint = MaterialTheme.colorScheme.secondary,
                contentDescription = "back button",
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .clickable() {
                        val id: Long? = activeUser?.id
                        if (id != null) {
                            scope.launch {
                                if (inputText.trim().isNotEmpty()) {
                                    username = inputText.trim()
                                    viewModel.updateUserName(id, inputText.trim())
                                    showEditUsernameDialog = false
                                    isUsernameInputError = false
                                }
                                else {
                                    isUsernameInputError = true
                                }
                            }
                        }

                        else {
                            showEditUsernameDialog = false
                            isUsernameInputError = false
                        }
                    }
                    .size(55.dp)
                    .padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
    }

    // ЯЗЫК
    ReusableDialog(
        isVisible = showChangeLanguageDialog,
        onDismiss = {
            showChangeLanguageDialog = false
        }
    ) {
        val options = listOf("Русский" to "ru", "English" to "en")
        var selectedOption by remember { mutableStateOf(options[0]) }

        Column {
            options.forEach { (displayName, langCode) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = displayName to langCode

                            scope.launch {
                                settings.saveLanguage(langCode)
                                changeAppLanguage(langCode)
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (currentLang == langCode),
                        onClick = { }
                    )
                    Text(
                        text = displayName,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 5.dp)
        ) {
            Spacer(modifier = Modifier.width(3.dp))
        }

        Spacer(modifier = Modifier.height(6.dp))
    }
}

fun goToAuth(
    context: android.content.Context,
    clearStack: Boolean = true,
    isSlideBack: Boolean = false,
    isFirst: Boolean = false
) {
    val intent = Intent(context, AuthActivity::class.java)

    if (clearStack) {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    else {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    if (isFirst) {
        intent.putExtra("IS_FIRST_AUTH", "yes")
    }

    val options = if (isSlideBack) {
        ActivityOptions.makeCustomAnimation(
            context,
            R.anim.slide_in_left,
            R.anim.slide_out_right
        )
    } else {
        ActivityOptions.makeCustomAnimation(
            context,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
    }

    context.startActivity(intent, options.toBundle())

    if (context is android.app.Activity && clearStack) {
        context.finish()
    }
}
