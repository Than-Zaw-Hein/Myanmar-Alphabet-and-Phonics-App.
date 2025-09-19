package com.tzh.mamp.provider

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.ui.screen.destinations.AlphabetScreenDestination
import com.tzh.mamp.ui.screen.destinations.SettingsScreenDestination
import com.tzh.mamp.ui.screen.destinations.VideoPlayerDestination

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

    override fun openToAlphabet(index: Int, type: AlphabetType) {
        navController.navigate(
            AlphabetScreenDestination(
                index,
                type
            ), navOptionsBuilder = {})
    }

    override fun openToVowel(index: Int) {

    }
}