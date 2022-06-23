package com.zaskha.storyapepe.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val store: DataStore<Preferences>) {

    suspend fun keepUser(user: UserModel) {
        store.edit {
            it[TOKEN] = user.token
            it[STATE] = user.hasLogin
        }
    }

    fun user(): Flow<UserModel> = store.data.map { UserModel(it[TOKEN] ?: "", it[STATE] ?: false) }

    suspend fun logout() {
        store.edit { it[STATE] = false }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val STATE = booleanPreferencesKey("state")
        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val userPreference = UserPreference(dataStore)
                this.INSTANCE = userPreference
                userPreference
            }
        }
    }
}