package com.watermuffin.todaylist.ui.screens.profile.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun changeAppLanguage(langCode: String) {
    val appLocale = LocaleListCompat.forLanguageTags(langCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
