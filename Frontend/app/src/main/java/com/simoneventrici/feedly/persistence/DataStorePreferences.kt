package com.simoneventrici.feedly.persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.simoneventrici.feedly.model.GeoLocalizationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStorePreferences(private val context: Context) {
    private val Context.appDataStore: DataStore<Preferences> by preferencesDataStore(name = "appStorage")
    private val dataStore = context.appDataStore

    private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    private val GEO_LOCALIZATION_KEY = stringPreferencesKey("geo_localization_info")
    private val FAVOURITE_LEAGUE_KEY = intPreferencesKey("favourite_team")
    private val NEWS_LANGUAGES_KEY = stringPreferencesKey("news_languages")

    suspend fun saveToken(token: String) {
        dataStore.edit { preference ->
            preference[AUTH_TOKEN_KEY] = token
        }
    }

    suspend fun saveGeoCoords(geoLocalizationInfo: GeoLocalizationInfo) {
        dataStore.edit { preference ->
            preference[GEO_LOCALIZATION_KEY] = geoLocalizationInfo.toString()
        }
    }

    suspend fun saveFavouriteLeague(leagueId: Int) {
        dataStore.edit { preference ->
            preference[FAVOURITE_LEAGUE_KEY] = leagueId
        }
    }

    suspend fun saveNewsLanguage(language: String) {
        dataStore.edit { preference ->
            preference[NEWS_LANGUAGES_KEY] = language
        }
    }

    //ogni volta che viene salvato qualcosa nel datastore, tutti i flussi emettono di nuovo un valore
    // è un flusso che emette un valore ogni volta che cambia il token salvato
    val tokensFlow: Flow<String?> = dataStore.data.map { preference -> preference[AUTH_TOKEN_KEY]}
    val favLeagueFlow: Flow<Int?> = dataStore.data.map { preference -> preference[FAVOURITE_LEAGUE_KEY] }
    val geoInfoFlow: Flow<GeoLocalizationInfo?> = dataStore.data.map { preference -> GeoLocalizationInfo.parseFromString(preference[GEO_LOCALIZATION_KEY] ?: "") }

    val newsLangFlow: Flow<String> = dataStore.data.map { preference -> preference[NEWS_LANGUAGES_KEY] ?: "en" }
}