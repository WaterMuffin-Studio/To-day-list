package com.watermuffin.todaylist.ui.screens.profile.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.watermuffin.todaylist.R
import com.watermuffin.todaylist.ui.theme.customColors
import java.io.File

@Composable
fun ProfileAvatarImage (
    username: String = "Guest",
    avatarPath: String?,
    onAvatarSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onAvatarSelected(it) }
    }
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(130.dp)
            .shadow(elevation = 10.dp, shape = CircleShape, clip = true)
            .background(MaterialTheme.colorScheme.background)
            .border(width = 2.dp, color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                    radius = 65.dp
                )
            ) {
                launcher.launch("image/*")
            },
        contentAlignment = Alignment.Center
    ) {
        if (avatarPath != null) {
            AsyncImage(
                model = File(context.filesDir, avatarPath),
                contentDescription = "AVATAR",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        else {
            Icon(painterResource(R.drawable.plus), contentDescription = "NO AVATAR", tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}