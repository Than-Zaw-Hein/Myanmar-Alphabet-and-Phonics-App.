package com.tzh.mamp.di

import android.content.Context
import com.tzh.framework.pref.CacheManager
import com.tzh.mamp.provider.AppLanguageProvider
import com.tzh.mamp.provider.AppResourceProvider
import com.tzh.mamp.provider.AppThemeProvider
import com.tzh.mamp.provider.LanguageProvider
import com.tzh.mamp.provider.ResourceProvider
import com.tzh.mamp.provider.ThemeProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProviderModule {

    @Provides
    @Singleton
    fun provideAppLanguageProvider(@ApplicationContext context: Context): LanguageProvider {
        return AppLanguageProvider(context)
    }

    @Provides
    @Singleton
    fun provideAppResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return AppResourceProvider(context)
    }

    @Provides
    @Singleton
    fun provideAppThemeProvider(@ApplicationContext context: Context): ThemeProvider {
        return AppThemeProvider(context)
    }
}