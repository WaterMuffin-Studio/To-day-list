package com.watermuffin.todaylist.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class CustomColors(
    val textPrimary: Color = Color.Unspecified,
    val avatarDefault: Color = Color.Unspecified,
    val screenTitle: Color = Color.Unspecified,
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

val MaterialTheme.customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current

private val LightCustomPalette = CustomColors(
    textPrimary = primaryTextColor,
    avatarDefault = avatarDefault,
    screenTitle = screenTitle,
)

private val DarkCustomPalette = CustomColors(
    textPrimary = primaryTextColorDark,
    avatarDefault = avatarDefaultDark,
    screenTitle = screenTitleDark,
)


private val DarkColorScheme = darkColorScheme(
    primary = primaryColorDark,
    secondary = secondaryColorDark,

    background = mainBackgroundColorDark,
    surface = mainBackgroundColorDark,
    onBackground = mainTitleColorDark,

)

private val LightColorScheme = lightColorScheme(
    primary = primaryColor,
    secondary = secondaryColor,

    background = mainBackgroundColor,
    surface = mainBackgroundColor,
    onBackground = mainTitleColor,


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun TodayListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val customColors = if (darkTheme) DarkCustomPalette else LightCustomPalette
    val colorScheme = when {
//      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//        val context = LocalContext.current
//        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//      }
      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalCustomColors provides customColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}