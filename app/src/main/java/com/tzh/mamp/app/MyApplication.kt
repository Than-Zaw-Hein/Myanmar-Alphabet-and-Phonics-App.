package com.tzh.mamp.app

import com.tzh.framework.base.app.AppInitializer
import com.tzh.framework.base.app.CoreApplication
import com.tzh.mamp.provider.AppLanguageProvider
import com.tzh.mamp.provider.LanguageProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : CoreApplication() {
    @Inject
    lateinit var initializer: AppInitializer

    @Inject
    lateinit var appLanguageProvider: LanguageProvider

    override fun onCreate() {
        super.onCreate()
        initializer.init(this)

        appLanguageProvider.setLocale(appLanguageProvider.getLanguageCode(), this)

    }
}