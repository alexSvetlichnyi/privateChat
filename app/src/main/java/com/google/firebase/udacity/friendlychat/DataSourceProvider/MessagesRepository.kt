package com.google.firebase.udacity.friendlychat.DataSourceProvider

import android.net.Uri
import com.google.firebase.udacity.friendlychat.FriendlyMessage
import kotlinx.coroutines.flow.StateFlow

interface MessagesRepository {
    fun postMessage(message: FriendlyMessage)
    fun attachDatabaseListener()
    fun detachDatabaseListener()
    fun provideCounter() : StateFlow<FriendlyMessage>
    fun postPhoto(uri: Uri?, userName: String, time: String)
}