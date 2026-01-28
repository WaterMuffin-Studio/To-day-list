package com.watermuffin.todaylist.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.watermuffin.todaylist.R
import com.watermuffin.todaylist.data.database.entities.UserEntity
import java.io.File

@Composable
fun SelectUserButton(
    username: String,
    avatarFileName: String?,
    onClick: () -> Unit,
    size: Int = 65,
    avatarSize: Int = 55,
    iconPath: Int = R.drawable.account_circle
) {
    val interactionSource = remember { MutableInteractionSource() }
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(size.dp)
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = true,
                )
            )
            .padding(horizontal = 20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (avatarFileName == null) {
                Icon(
                    painter = painterResource(iconPath),
                    contentDescription = "profile without avatar icon",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                    modifier = Modifier
                        .size(avatarSize.dp)
//                        .shadow(elevation = 3.dp, shape = CircleShape, clip = true)
//                        .background(MaterialTheme.colorScheme.background)
//                        .border(width = 1.dp, color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                )
            }
            else {
                Box(
                    modifier = Modifier
                        .size(avatarSize.dp)
                        .shadow(elevation = 3.dp, shape = CircleShape, clip = true)
                        .background(MaterialTheme.colorScheme.background)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.secondary, shape = CircleShape)
                ) {
                    AsyncImage (
                        model = File(context.filesDir, avatarFileName),
                        contentDescription = "profile avatar",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = username,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Icon(
            painter = painterResource(R.drawable.chevron_right),
            contentDescription = "change profile icon",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
        )
    }
}