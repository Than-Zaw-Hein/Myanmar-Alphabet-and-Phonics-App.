package com.tzh.mamp.ui.navigation.navHost

import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.tzh.mamp.provider.AppNavigationProvider
import com.tzh.mamp.ui.screen.NavGraphs

@Composable
fun MainRoot(
) {
    val navController = rememberNavController()
    val destinationsNavigator = remember { AppNavigationProvider(navController) }
    DestinationsNavHost(
        navController = navController,
        navGraph = NavGraphs.root,
        dependenciesContainerBuilder = {
            dependency(destinationsNavigator)
        }
    ) {

    }
}