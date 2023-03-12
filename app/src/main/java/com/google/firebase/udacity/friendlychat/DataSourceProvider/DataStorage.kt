package com.google.firebase.udacity.friendlychat.DataSourceProvider

import android.net.Uri
import com.google.firebase.udacity.friendlychat.FriendlyMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DataStorage {
    suspend fun setUserID(uid: String)
    suspend fun getUserID() : Flow<String>
}