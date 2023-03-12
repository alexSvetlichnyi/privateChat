package com.google.firebase.udacity.friendlychat

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.udacity.friendlychat.DataSourceProvider.DataStorage
import com.google.firebase.udacity.friendlychat.DataSourceProvider.MessagesRepository
import com.google.firebase.udacity.friendlychat.application.BaseAndroidViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FriendlyChatViewModel @Inject constructor(
        private val dataSource: MessagesRepository,
        private val dataStorage: DataStorage,
        private val firebaseAuth: FirebaseAuth
) : BaseAndroidViewModel(), Observable, LifecycleObserver {

    var messageAdapter : MessageAdapter = MessageAdapter()
    val progressVisibility = ObservableInt(View.GONE)
    val sendButtonEnabled = ObservableBoolean()
    val startLoginFlow: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    val openPhotoChooser: MutableLiveData<Unit> by lazy { MutableLiveData<Unit>() }
    var userName = ANONYMOUS
    lateinit var currenUser : FirebaseUser

    var job : Job = Job()
    private val value = FirebaseAuth.AuthStateListener { auth ->
        if (auth.currentUser != null) {
            currenUser = auth.currentUser
            viewModelScope.launch {
                dataStorage.setUserID(currenUser.uid)
            }
            userName = auth.currentUser?.displayName ?: ANONYMOUS
            dataSource.attachDatabaseListener()
            startCollection()
        } else {
            stopMessages()
            startLoginFlow.value = null
        }
    }


    private fun stopMessages() {
        dataSource.detachDatabaseListener()
        messageAdapter.clear()
        job.cancel()
    }

    @Bindable
    var newMessage = ""
        set(value) {
            sendButtonEnabled.set(value.trim().isNotEmpty())
            field = value

        }

    private fun startCollection() {
        job = viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            dataSource.provideCounter().collect {
                messageAdapter.addNewMessage(it)
            }
        }
    }

    fun onImageClick() {
        openPhotoChooser.value = null
    }

    fun onSendClick() {
        val formatedDate = getCurrentTime()
        if (userName != KOSTYA) {
            userName = KOSTYA
        } else {
            userName = currenUser.displayName ?: ANONYMOUS
        }
        val friendlyMessage = FriendlyMessage(newMessage, userName, formatedDate, null, currenUser.uid, isCurrentUser = userName == currenUser.displayName ?: ANONYMOUS)
        dataSource.postMessage(friendlyMessage)
        newMessage = ""
        notifyPropertyChanged(BR.newMessage)
    }

    private fun getCurrentTime(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat.getTimeInstance()
        return formatter.format(date)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        firebaseAuth.addAuthStateListener(value)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        firebaseAuth.removeAuthStateListener(value)
        stopMessages()
    }

    fun postPhoto(uri: Uri?) {
        dataSource.postPhoto(uri, userName, getCurrentTime())
    }

    companion object {
        const val ANONYMOUS = "anonymous"
        const val KOSTYA = "Костя"
        const val RC_SIGN_IN = 1
        const val RC_PHOTO_PICKER = 2
    }
}
