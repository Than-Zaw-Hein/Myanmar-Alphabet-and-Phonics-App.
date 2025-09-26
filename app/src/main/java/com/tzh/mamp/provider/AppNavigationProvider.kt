package com.tzh.mamp.provider

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.ui.screen.destinations.AlphabetScreenDestination
import com.tzh.mamp.ui.screen.destinations.ConsonantTracingScreenDestination
import com.tzh.mamp.ui.screen.destinations.SettingsScreenDestination
import com.tzh.mamp.ui.screen.destinations.VideoPlayerDestination

class AppNavigationProvider(
    private val navController: NavController,
) : NavigationProvider {
    val multipleEventsCutter = MultipleEventsCutter.get()

    override fun openSetting() {
        multipleEventsCutter.processEvent {
            navController.navigate(SettingsScreenDestination, navOptionsBuilder = {})
        }
    }

    override fun openVideoPlayer() {
        multipleEventsCutter.processEvent {
            navController.navigate(
                VideoPlayerDestination,
                navOptionsBuilder = {})
        }
    }

    override fun onBack() {
        multipleEventsCutter.processEvent { navController.popBackStack() }
    }

    override fun openToAlphabet(index: Int, type: AlphabetType) {
        multipleEventsCutter.processEvent {
            navController.navigate(
                AlphabetScreenDestination(
                    index,
                    type
                ), navOptionsBuilder = {})
        }
    }


    override fun openTracing(consonant: String) {
        multipleEventsCutter.processEvent {
            navController.navigate(
                ConsonantTracingScreenDestination(
                    consonant = consonant
                ), navOptionsBuilder = {}
            )
        }
    }
}


interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)

    companion object
}

internal fun MultipleEventsCutter.Companion.get(): MultipleEventsCutter =
    MultipleEventsCutterImpl()

private class MultipleEventsCutterImpl : MultipleEventsCutter {
    private val now: Long
        get() = System.currentTimeMillis()

    private var lastEventTimeMs: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (now - lastEventTimeMs >= 300L) {
            event.invoke()
        }
        lastEventTimeMs = now
    }
}
