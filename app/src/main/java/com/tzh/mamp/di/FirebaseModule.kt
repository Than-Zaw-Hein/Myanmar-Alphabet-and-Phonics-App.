// Create a new file in /di/FirebaseModule.kt
package com.tzh.mamp.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance("https://myanmar-alphabet-and-phonics-default-rtdb.asia-southeast1.firebasedatabase.app/")
    }
}
