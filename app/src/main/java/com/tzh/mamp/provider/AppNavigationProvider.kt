package com.tzh.mamp.provider

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.tzh.mamp.ui.screen.VideoPlayer
import com.tzh.mamp.ui.screen.destinations.SettingsScreenDestination
import com.tzh.mamp.ui.screen.destinations.VideoPlayerDestination
import timber.log.Timber

class AppNavigationProvider(
    private val navController: NavController,
) : NavigationProvider {
    override fun openSetting() {
        navController.navigate(SettingsScreenDestination, navOptionsBuilder = {})
    }

    override fun openVideoPlyaer() {
        navController.navigate(VideoPlayerDestination, navOptionsBuilder = {})
    }

    override fun onBack() {
        navController.popBackStack()
    }
}