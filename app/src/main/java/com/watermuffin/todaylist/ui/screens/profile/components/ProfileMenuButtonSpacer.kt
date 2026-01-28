package com.watermuffin.todaylist.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProfileMenuButtonSpacer (
    color: Color = MaterialTheme.colorScheme.secondary,
    space: Int = 2
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = space.dp)
    ) {
        Spacer(modifier = Modifier.height(space.dp))
        Box(modifier = Modifier.fillMaxWidth().background(color).height(1.dp))
        Spacer(modifier = Modifier.height(space.dp))
    }
}