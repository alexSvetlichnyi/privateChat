package com.google.firebase.udacity.friendlychat.DataSourceProvider

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.udacity.friendlychat.FriendlyMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton
private const val USER_PREFERENCES_NAME = "user_preferences"

private val Context.dataStore by preferencesDataStore(
        name = USER_PREFERENCES_NAME
)

@Singleton
class DataStorageImpl @Inject constructor (
        @ApplicationContext context: Context
) : DataStorage {
    private val dataStore = context.dataStore
    private val USER_ID = stringPreferencesKey("userID")

    override suspend fun setUserID(uid: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = uid
        }
    }

    override suspend fun getUserID() : Flow<String>  =
        dataStore.data
                .map { preferences ->
                    preferences[USER_ID] ?: ""
                }
}