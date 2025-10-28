package com.qverzey.drawtogether.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.sessionDataStore by preferencesDataStore("session_prefs")

class SessionManager(private val context: Context) {

    companion object {
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_PASSWORD = stringPreferencesKey("password")  // ⚠️ only for local usage
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    val isLoggedIn: Flow<Boolean> = context.sessionDataStore.data
        .map { it[KEY_IS_LOGGED_IN] ?: false }

    val userId: Flow<String?> = context.sessionDataStore.data
        .map { it[KEY_USER_ID] }

    val password: Flow<String?> = context.sessionDataStore.data
        .map { it[KEY_PASSWORD] }

    suspend fun saveSession(userId: String, password: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_USER_ID] = userId
            prefs[KEY_PASSWORD] = password
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { it.clear() }
    }
}