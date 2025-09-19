package com.tzh.mamp.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
import com.google.firebase.firestore.ktx.persistentCacheSettings
import com.tzh.framework.base.app.AppInitializer
import com.tzh.framework.base.app.AppInitializerImpl
import com.tzh.framework.base.app.FirebaseInitializer
import com.tzh.framework.base.app.NetworkConfig
import com.tzh.framework.base.app.TimberInitializer
import com.tzh.framework.pref.CacheManager
import com.tzh.mamp.app.AppNetworkConfig
import com.tzh.mamp.app.MyApplication
import com.tzh.mamp.data.data_store.AppConfiguration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApplication(): MyApplication {
        return MyApplication()
    }

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig {
        return AppNetworkConfig()
    }

    @Singleton
    @Provides
    fun providesAppConfiguration(@ApplicationContext context: Context): AppConfiguration {
        return AppConfiguration(context)
    }

    @Provides
    @Singleton
    fun provideCacheManager(@ApplicationContext context: Context): CacheManager {
        return CacheManager(context)
    }

    @Provides
    @Singleton
    fun provideTimberInitializer(
        networkConfig: NetworkConfig,
    ) = TimberInitializer(networkConfig.isDev())

    @Provides
    @Singleton
    fun provideFirebaseInitializer(
    ) = FirebaseInitializer()

    @Provides
    @Singleton
    fun provideAppInitializer(
        timberInitializer: TimberInitializer,
        firebaseInitializer: FirebaseInitializer,
    ): AppInitializer {
        return AppInitializerImpl(
            timberInitializer,
            firebaseInitializer
        )
    }


    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance().apply {
        val settings = firestoreSettings {
            // Use memory cache
            setLocalCacheSettings(memoryCacheSettings {})
            // Use persistent disk cache (default)
            setLocalCacheSettings(persistentCacheSettings {})
        }
        firestoreSettings = settings
    }


}