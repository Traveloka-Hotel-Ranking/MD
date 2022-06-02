package com.traveloka.hotelranking.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[NAME_KEY] ?:"",
                pref[EMAIL_KEY] ?:"",
                pref[ACCESS_TOKEN_KEY] ?:"",
                pref[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(userModel: UserModel) {
        dataStore.edit { pref ->
            pref[NAME_KEY] = userModel.name
            pref[EMAIL_KEY] = userModel.email
            pref[ACCESS_TOKEN_KEY] = userModel.accessToken
        }
    }

    suspend fun login() {
        dataStore.edit { pref ->
            pref[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { pref ->
            pref.clear()
            pref[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val STATE_KEY = booleanPreferencesKey("state_token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}