package com.keecoding.githubuser.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PrefViewModel(private val app: Application) : ViewModel() {
    private val _darkMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val darkMode = _darkMode as LiveData<Boolean>

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val themeKey = booleanPreferencesKey("theme")

    init {
        readTheme()
    }

    private fun readTheme() {
        viewModelScope.launch(Dispatchers.Default) {
            val themeFlow: Flow<Boolean> = app.dataStore.data
                .map { preferences ->
                    preferences[themeKey] ?: false
                }

            themeFlow.collect {
                _darkMode.postValue(it)
            }
        }
    }

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            if (isDark == darkMode.value) return@launch
            app.dataStore.edit { settings ->
                val currentTheme = settings[themeKey] ?: false
                settings[themeKey] = !currentTheme
            }
        }
    }
}