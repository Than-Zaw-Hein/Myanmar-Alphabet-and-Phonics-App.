package com.tzh.mamp.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tzh.mamp.R
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.provider.ResourceProvider
import com.tzh.mamp.ui.navigation.route.Screen
import com.tzh.mamp.ui.screen.consonant_letters.ConsonantLettersScreen
import com.tzh.mamp.ui.screen.vowels.VowelLetterScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun MainScreen(
    navProvider: NavigationProvider,
    screenDrawers: List<Screen> = Screen.drawer
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen: String by rememberSaveable { mutableStateOf(screenDrawers.first().route) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    painter = painterResource(R.mipmap.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Myanmar Alphabet And Phonics",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                screenDrawers.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(stringResource(screen.title)) },
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = stringResource(screen.title)
                            )
                        },
                        selected = screen.route == currentScreen,
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
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(stringResource(R.string.myanmar_alphabet_phonics)) }, navigationIcon = {
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

                    Screen.VowelLetters.route -> {
                        VowelLetterScreen(navProvider = navProvider)
                    }

                    Screen.ConsonantLetters.route -> {
                        ConsonantLettersScreen(navProvider = navProvider)
                    }

                    Screen.Quiz.route -> {
                        ConsonantLettersScreen(navProvider = navProvider)
                    }

                    Screen.Tracing.route -> {
                        ConsonantLettersScreen(navProvider = navProvider)
                    }
                }
            }
        }
    }
}