package com.tzh.mamp.ui.navigation.navHost

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.tzh.mamp.provider.AppNavigationProvider
import com.tzh.mamp.provider.AppResourceProvider
import com.tzh.mamp.ui.screen.NavGraphs

@Composable
fun MainRoot(
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val destinationsNavigator = remember { AppNavigationProvider(navController) }
    DestinationsNavHost(
        modifier = Modifier,
        navController = navController,
        navGraph = NavGraphs.root,
        dependenciesContainerBuilder = {
            dependency(destinationsNavigator)
        }) {

    }
}