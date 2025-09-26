package com.tzh.mamp.provider

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.content.edit
import com.tzh.framework.pref.getPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.*

class AppLanguageProvider(private val context: Context) : LanguageProvider {

    companion object {
        private const val LANG_CODE = "lang_code"
        private const val DEFAULT_LANGUAGE = "en"
    }

    private val sharedPreferences = context.getPrefs()

    private val preferenceKeyChangedFlow = MutableSharedFlow<String>(extraBufferCapacity = 1)

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        preferenceKeyChangedFlow.tryEmit(key ?: LANG_CODE)
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun saveLanguageCode(languageCode: String) {
        sharedPreferences.edit {
            putString(LANG_CODE, languageCode)
        }
    }

    override fun getLanguageCode(): String {
        return sharedPreferences.getString(LANG_CODE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    override fun setLocale(language: String, context: Context) {
        updateResources(language, context)
    }

    override fun observeLanguageCode(): Flow<String> {
        return preferenceKeyChangedFlow
            // Emit on start so we always send the initial value
            .onStart { emit(LANG_CODE) }
            .filter { it == LANG_CODE }
            .map { getLanguageCode() }
            .distinctUntilChanged()
    }

    private fun updateResources(language: String, context: Context) {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        // Updates app-wide configuration
        context.createConfigurationContext(configuration)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}