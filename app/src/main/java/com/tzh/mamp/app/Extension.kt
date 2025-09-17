package com.tzh.mamp.app

import android.app.Activity
object Extension {
    fun Activity.setScreenOrientation(orientation: Int) {
        requestedOrientation = orientation
    }
}

