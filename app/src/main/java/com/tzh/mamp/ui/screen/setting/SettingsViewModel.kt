package com.tzh.mamp.ui.screen.setting

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tzh.mamp.provider.LanguageProvider
import com.tzh.mamp.provider.ThemeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val languageProvider: LanguageProvider,
    private val themeProvider: ThemeProvider
) : ViewModel() {

    val themeFlow: Flow<ThemeProvider.Theme> = themeProvider.observeTheme()

    fun setTheme(theme: ThemeProvider.Theme) {
        themeProvider.theme = theme
    }

    fun setLanguage(code: String, context: Context) {
        languageProvider.saveLanguageCode(code)
        languageProvider.setLocale(code, context)
    }

    fun getCurrentLanguage(): String {
        return languageProvider.getLanguageCode()
    }

    fun isNightMode(): Boolean = themeProvider.isNightMode()
}
