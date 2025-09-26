package com.tzh.mamp.app

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.tzh.framework.base.app.AppInitializer
import com.tzh.framework.base.app.CoreApplication
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.tzh.mamp.BuildConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApplication : CoreApplication() {
    @Inject
    lateinit var initializer: AppInitializer

    override fun onCreate() {
        super.onCreate()
        initializer.init(this)
        MobileAds.initialize(this) {}
        // Configure test devices (only in debug builds!)
        if (BuildConfig.DEBUG) {
            val configuration = RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("YOUR_TEST_DEVICE_ID"))
                .build()
            MobileAds.setRequestConfiguration(configuration)
        }

    }
}