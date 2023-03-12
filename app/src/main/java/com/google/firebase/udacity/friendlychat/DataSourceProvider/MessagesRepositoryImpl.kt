package com.google.firebase.udacity.friendlychat.DataSourceProvider

import android.net.Uri
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.udacity.friendlychat.FriendlyMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(firebaseDatabase: FirebaseDatabase, firebaseStorage: FirebaseStorage) : MessagesRepository {

    private val databaseReference = firebaseDatabase.getReference("messages")
    private val storageReference = firebaseStorage.getReference("chat_photos")
    private val message = MutableStateFlow(FriendlyMessage())
    private val counter: StateFlow<FriendlyMessage> = message
    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val friendlyMessage = snapshot.getValue(FriendlyMessage::class.java)
                    ?: FriendlyMessage()
            message.value = friendlyMessage
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            //TODO("Not yet implemented")
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            //TODO("Not yet implemented")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            //TODO("Not yet implemented")
        }

        override fun onCancelled(error: DatabaseError) {
            //TODO("Not yet implemented")
        }
    }

    override fun postMessage(message: FriendlyMessage) {
        databaseReference.push().setValue(message)
    }

    override fun attachDatabaseListener() {
        databaseReference.addChildEventListener(childEventListener)
    }

    override fun detachDatabaseListener() {
        databaseReference.removeEventListener(childEventListener)
    }

    override fun provideCounter() : StateFlow<FriendlyMessage> = counter

    override fun postPhoto(uri: Uri?, userName: String, time: String) {
        uri?.let {
            val ref = storageReference.child(uri.lastPathSegment ?: "")
            val uploadTask = ref.putFile(uri)
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    databaseReference.push().setValue(FriendlyMessage(null, userName, time, downloadUri.toString()))
                } else {
                    // Handle failures
                }
            }
        }
    }
}
