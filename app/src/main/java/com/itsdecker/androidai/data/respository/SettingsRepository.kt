package com.itsdecker.androidai.data.respository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "com.itsdecker.mai.data_store")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val chatRepository: ChatRepository,
) {
    private val dataStore = context.settingsDataStore

    // Default API Key
    private val defaultApiKeyIdPref = stringPreferencesKey("default_api_key_id")
    private val defaultApiKeyId: Flow<String?> = dataStore.data.map { preferences -> preferences[defaultApiKeyIdPref] }

    suspend fun getDefaultApiKeyId(): String? = withContext(Dispatchers.IO) {
        defaultApiKeyId.firstOrNull()
            ?: chatRepository.getLatestApiKey()?.let { apiKey ->
                setDefaultApiKeyId(apiKeyId = apiKey.id)
                apiKey.id
            }
    }

    suspend fun setDefaultApiKeyId(apiKeyId: String) = withContext(Dispatchers.IO) {
        dataStore.updateData { preferences ->
            preferences.toMutablePreferences().apply {
                set(defaultApiKeyIdPref, apiKeyId)
            }
        }
    }
}