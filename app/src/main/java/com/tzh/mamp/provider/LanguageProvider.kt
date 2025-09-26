package com.tzh.mamp.provider

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface LanguageProvider {
    fun saveLanguageCode(languageCode: String)
    fun getLanguageCode(): String
    fun setLocale(language: String, context: Context)
    fun observeLanguageCode(): Flow<String>
}