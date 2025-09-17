package com.tzh.mamp.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.navigation.route.Screen
import com.tzh.mamp.ui.screen.home.HomeScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Destination(start = true)
@Composable
fun MainScreen(
    navProvider: NavigationProvider,
    screenDrawers: List<Screen> = Screen.drawer
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen: String by rememberSaveable(
    ) { mutableStateOf(screenDrawers.first().route) }

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Myanmar Kids App",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                screenDrawers.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.title) },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            if (currentScreen != screen.route) {
                                currentScreen = screen.route
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Myanmar Alphabet & Phonics") }, navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                }, actions = {
                    IconButton(onClick = {
                        navProvider.openSetting()
                    }) {
                        Icon(Icons.Default.Settings, contentDescription = "Setting")
                    }
                })
            }) { innerPadding ->
            AnimatedContent(
                currentScreen, modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { screen ->
                when (screen) {
                    Screen.Alphabet.route -> {
                        HomeScreen(navProvider)
                    }

                    Screen.Home.route -> {
                        HomeScreen(navProvider)
                    }

                    Screen.Quiz.route -> {
                        HomeScreen(navProvider)
                    }

                    Screen.Tracing.route -> {
                        HomeScreen(navProvider)
                    }
                }
            }
        }
    }
}