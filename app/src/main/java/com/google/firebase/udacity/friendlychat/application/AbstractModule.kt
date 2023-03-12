package com.google.firebase.udacity.friendlychat.application

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.udacity.friendlychat.DataSourceProvider.DataStorage
import com.google.firebase.udacity.friendlychat.DataSourceProvider.DataStorageImpl
import com.google.firebase.udacity.friendlychat.DataSourceProvider.MessagesRepository
import com.google.firebase.udacity.friendlychat.DataSourceProvider.MessagesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AbstractModule{
    @Binds
    abstract fun bindMessagesRepo(messagesRepositoryImpl: MessagesRepositoryImpl): MessagesRepository

    @Binds
    abstract fun bindDataStore(dataStorage: DataStorageImpl): DataStorage
}