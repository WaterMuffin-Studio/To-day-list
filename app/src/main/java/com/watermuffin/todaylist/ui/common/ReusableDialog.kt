package com.watermuffin.todaylist.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.watermuffin.todaylist.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ReusableDialog (
    isVisible: Boolean,
    onDismiss: () -> Unit,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        usePlatformDefaultWidth = false
    ),
    isWithCloseButton: Boolean = false,
    content: @Composable () -> Unit
) {
    if (isVisible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = properties
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isWithCloseButton) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .height(40.dp)
                                        .width(40.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.close),
                                        contentDescription = "close dialog",
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier
                                            .height(32.dp)
                                            .width(32.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        content()
                    }
                }
            }
        }
    }
}