package com.tzh.mamp.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.tzh.framework.network.createMoshi
import com.tzh.framework.network.createOkHttpClient
import com.tzh.framework.network.createRetrofitWithMoshi
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.data.network.YoutubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        // Using the createMoshi function from your framework, but adding the KotlinJsonAdapterFactory
        // since the YouTube response models are data classes.
        return createMoshi().newBuilder()
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        // Using the createOkHttpClient function from your framework
        // isDev is set based on the app's BuildConfig.DEBUG flag
        return createOkHttpClient(
            isDev = BuildConfig.DEBUG,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideYoutubeApiService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): YoutubeApiService {
        // Using the generic createRetrofitWithMoshi function from your framework
        return createRetrofitWithMoshi(
            okHttpClient = okHttpClient,
            moshi = moshi,
            baseUrl = YOUTUBE_BASE_URL
        )
    }
}
