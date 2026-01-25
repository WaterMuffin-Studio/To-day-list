package com.watermuffin.todaylist.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.watermuffin.todaylist.R
import com.watermuffin.todaylist.ui.theme.customColors

@Composable
fun RowScope.NavItem (
    route: String,
    currentRoute: String,
    icon: Int,
    iconIfSelected: Int,
    label: String,
    onClick: () -> Unit
) {
    val isSelected = currentRoute == route
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val textTopPadding by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 4.dp,
        animationSpec = tween(300)
    )

    val contentColor = if (isSelected) {
        MaterialTheme.customColors.textPrimary
    } else {
        MaterialTheme.colorScheme.onBackground
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = ripple(
                    bounded = false,
                    radius = 40.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = if (isSelected) iconIfSelected else icon),
                contentDescription = label,
                modifier = Modifier.size(30.dp),
                tint = contentColor
            )

            Text(
                text = label,
                modifier = Modifier.padding(top = textTopPadding),
                fontSize = if (isSelected) 12.sp else 11.sp,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontFamily = FontFamily(Font(R.font.lato_medium)),
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
//            .clip(RoundedCornerShape(0.dp))
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            route = "statistics",
            currentRoute = currentRoute,
            icon = R.drawable.chart_box_outline,
            iconIfSelected = R.drawable.chart_box,
            label = "Прогресс",
            onClick = { onNavigate("statistics") }
        )

        NavItem(
            route = "calendar",
            currentRoute = currentRoute,
            icon = R.drawable.calendar_month_outline,
            iconIfSelected = R.drawable.calendar_month,
            label = "Календарь",
            onClick = { onNavigate("calendar") }
        )

        NavItem(
            route = "todays",
            currentRoute = currentRoute,
            icon = R.drawable.book_open_variant_outline,
            iconIfSelected = R.drawable.book_open_variant,
            label = "Главная",
            onClick = { onNavigate("todays") }
        )

        NavItem(
            route = "notes",
            currentRoute = currentRoute,
            icon = R.drawable.note_edit_outline,
            iconIfSelected = R.drawable.note_edit,
            label = "Заметки",
            onClick = { onNavigate("notes") }
        )

        NavItem(
            route = "profile",
            currentRoute = currentRoute,
            icon = R.drawable.account_circle_outline,
            iconIfSelected = R.drawable.account_circle,
            label = "Профиль",
            onClick = { onNavigate("profile") }
        )
    }
}