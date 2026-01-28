package com.watermuffin.todaylist

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.watermuffin.todaylist.data.store.AppSettings
import com.watermuffin.todaylist.ui.screens.profile.components.changeAppLanguage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "todaylist_settings"
)

@HiltAndroidApp
class TodayListApplication : Application() {
    lateinit var languageManager: AppSettings

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        languageManager = AppSettings(applicationContext.dataStore)

        GlobalScope.launch {
            val savedLang = languageManager.getSavedLanguage()
            changeAppLanguage(savedLang)
        }
    }
}