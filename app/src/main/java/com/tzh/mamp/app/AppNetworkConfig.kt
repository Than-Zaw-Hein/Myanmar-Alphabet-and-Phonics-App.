package com.tzh.mamp.app

import com.tzh.framework.base.app.NetworkConfig
import com.tzh.mamp.BuildConfig

class AppNetworkConfig : NetworkConfig() {
    override fun baseUrl(): String {
        return "http://www.example.com"
    }

    override fun timeOut(): Long {
        return 30L
    }

    override fun isDev(): Boolean {
        return BuildConfig.DEBUG
    }
}