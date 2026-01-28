package com.watermuffin.todaylist.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppSettings(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_START_PAGE = stringPreferencesKey("start_page")
    }

    suspend fun saveLanguage(langCode: String) {
        dataStore.edit { it[KEY_LANGUAGE] = langCode }
    }

    suspend fun getSavedLanguage(): String {
        return dataStore.data.first()[KEY_LANGUAGE] ?: "ru"
    }

    val languageFlow = dataStore.data
        .map { it[KEY_LANGUAGE] ?: "ru" }

    data class Settings(
        val language: String,
        val theme: String,
        val startPage: String,
    )

    val settingsFlow = dataStore.data.map {
        Settings(
            language = it[KEY_LANGUAGE] ?: "ru",
            theme = it[KEY_THEME] ?: "system",
            startPage = it[KEY_START_PAGE] ?: "today",
        )
    }
}