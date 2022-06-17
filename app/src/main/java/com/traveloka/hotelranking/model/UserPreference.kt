package com.traveloka.hotelranking.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference (private val dataStore: DataStore<Preferences>){
    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { pref ->
            UserModel(
                pref[ID_KEY] ?: "",
                pref[NAME_KEY] ?:"",
                pref[EMAIL_KEY] ?:"",
                pref[PHONE_KEY] ?:"",
                pref[FAV_COUNTRY_KEY] ?:"",
                pref[FAV_FOOD_KEY] ?:"",
                pref[FAV_MOVIE_KEY] ?:"",
                pref[ACCESS_TOKEN_KEY] ?:"",
                pref[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(userModel: UserModel) {
        dataStore.edit { pref ->
            pref[ID_KEY] = userModel.id
            pref[NAME_KEY] = userModel.name
            pref[EMAIL_KEY] = userModel.email
            pref[PHONE_KEY] = userModel.phone
            pref[FAV_COUNTRY_KEY] = userModel.favCountry
            pref[FAV_FOOD_KEY] = userModel.favFood
            pref[FAV_MOVIE_KEY] = userModel.favMovie
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
        private val ID_KEY = stringPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val FAV_COUNTRY_KEY = stringPreferencesKey("fav_country")
        private val FAV_FOOD_KEY = stringPreferencesKey("fav_food")
        private val FAV_MOVIE_KEY = stringPreferencesKey("fav_movie")
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val STATE_KEY = booleanPreferencesKey("state_token")
    }
}