package com.tzh.framework.base.app

import com.google.firebase.FirebaseApp

class FirebaseInitializer() : AppInitializer {
    override fun init(application: CoreApplication) {

        // Initialize Firebase here if needed
        // For example, you can initialize Firebase Analytics or any other Firebase services
//        FirebaseApp.initializeApp(application)

        // If you want to set up Firebase Analytics, you can do it like this:
        // val firebaseAnalytics = FirebaseAnalytics.getInstance(application)
        // firebaseAnalytics.setAnalyticsCollectionEnabled(true)

        // You can also log events or set user properties as needed
    }
}