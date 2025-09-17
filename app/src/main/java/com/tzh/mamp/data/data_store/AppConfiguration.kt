package com.tzh.mamp.data.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppConfiguration @Inject constructor(private val context: Context) {
    companion object {
        private const val CONFIG_NAME = "settings"
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(CONFIG_NAME)

        val API_URL = stringPreferencesKey("api_url")
        val POWER = intPreferencesKey("reader_power")

        val FIRST_TIME_LOGIN = booleanPreferencesKey("first_time_login")

        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val USER_ID = longPreferencesKey("user_id")
        val FULL_NAME = stringPreferencesKey("full_name")
        val ROLE = stringPreferencesKey("role")
        val ProfileBackgroundColor = stringPreferencesKey("ProfileColor")

        val TOKEN_EXPIRED_DATE = stringPreferencesKey("token_expired_date")
    }

    suspend fun setToken(accessToken: String, refreshToken: String, expiredDate: String) {
        context.dataStore.edit {
            it[ACCESS_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
            it[TOKEN_EXPIRED_DATE] = expiredDate
        }
    }

    fun getAccessToken() = context.dataStore.data.map { it[ACCESS_TOKEN] ?: "" }
    fun getRefreshToken() = context.dataStore.data.map { it[REFRESH_TOKEN] ?: "" }
    fun getExpiredDate() = context.dataStore.data.map { it[TOKEN_EXPIRED_DATE] }

    suspend fun updateBaseUrl(url: String) {
        context.dataStore.edit {
            it[API_URL] = url
        }
    }

    fun getUrl(): Flow<String> {
        return context.dataStore.data.map { it[API_URL] ?: "" }
    }

    suspend fun setFirstTimeLogin() {
        context.dataStore.edit {
            it[FIRST_TIME_LOGIN] = true
        }
    }

    fun getIsFirstTimeLogin() = context.dataStore.data.map { it[FIRST_TIME_LOGIN] == true }

}