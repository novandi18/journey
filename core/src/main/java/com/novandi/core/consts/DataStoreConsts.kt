package com.novandi.core.consts

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreConsts {
    const val DATA_PREFERENCES = "data_preferences"
    val WELCOME_KEY = booleanPreferencesKey("welcome_key")
    val ROLE_KEY = intPreferencesKey("role_key")
    val ACCOUNT_ID_KEY = stringPreferencesKey("account_id_key")
    val TOKEN_KEY = stringPreferencesKey("token_key")
}