package com.novandi.core.data.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.novandi.core.consts.DataStoreConsts
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val isWelcomed: Flow<Boolean?> = dataStore.data.map { preferences ->
        preferences[DataStoreConsts.WELCOME_KEY] ?: false
    }

    val roleId: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[DataStoreConsts.ROLE_KEY] ?: 0
    }

    val accountId: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DataStoreConsts.ACCOUNT_ID_KEY] ?: ""
    }

    val token: Flow<String?> = dataStore.data.map { preferences ->
        preferences[DataStoreConsts.TOKEN_KEY] ?: ""
    }

    suspend fun setIsWelcome(isWelcome: Boolean) {
        dataStore.edit { preferences ->
            preferences[DataStoreConsts.WELCOME_KEY] = isWelcome
        }
    }

    suspend fun setRoleId(roleId: Int) {
        dataStore.edit { preferences ->
            preferences[DataStoreConsts.ROLE_KEY] = roleId
        }
    }

    suspend fun setAccountId(accountId: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreConsts.ACCOUNT_ID_KEY] = accountId
        }
    }

    suspend fun setToken(tokenValue: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreConsts.TOKEN_KEY] = tokenValue
        }
    }
}