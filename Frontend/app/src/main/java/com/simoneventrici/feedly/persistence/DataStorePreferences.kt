package com.simoneventrici.feedly.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferences(private val context: Context) {
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "appStorage")
    private val dataStore = context.appDataStore

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")

    suspend fun saveToken(token: String) {
        dataStore.edit { preference ->
            preference[AUTH_TOKEN_KEY] = token
        }
    }

    // è un flusso che emette un valore ogni volta che cambia il token salvato
    val tokensFlow: Flow<String?> = dataStore.data.map { preference -> preference[AUTH_TOKEN_KEY]}
}