package com.tzh.mamp.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.tzh.mamp.R
import com.tzh.mamp.app.util.QuizMode
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.BoxWithBackground
import com.tzh.mamp.ui.component.CustomDrawer
import com.tzh.mamp.ui.component.MyTopAppBar
import com.tzh.mamp.ui.component.coloredShadow
import com.tzh.mamp.ui.navigation.route.Screen
import com.tzh.mamp.ui.screen.consonant_letters.ConsonantLettersScreen
import com.tzh.mamp.ui.screen.feedback.FeedbackScreen
import com.tzh.mamp.ui.screen.quiz.ConsonantQuizScreen
import com.tzh.mamp.ui.screen.vowels.VowelLetterScreen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

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
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    val screenWidth = remember {
        derivedStateOf { (configuration.screenWidthDp * density).roundToInt() }
    }
    val offsetValue by remember { derivedStateOf { (screenWidth.value / 4.5).dp } }
    val animatedOffset by animateDpAsState(
        targetValue = if (drawerState.isOpen) offsetValue else 0.dp,
        label = "Animated Offset"
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpen) 0.9f else 1f,
        label = "Animated Scale"
    )

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    BackHandler(enabled = (currentScreen != screenDrawers.first().route) && drawerState.isClosed) {
        scope.launch {
            currentScreen = screenDrawers.first().route
        }
    }

    Box(
        modifier = Modifier
            .background(Color.Black)
            .background(Color.DarkGray.copy(alpha = 0.05f))
            .fillMaxSize()
    ) {
        CustomDrawer(
            isOpenDrawer = drawerState.isOpen,
            screenDrawers = screenDrawers,
            selectedScreenDrawers = currentScreen,
            onNavigationItemClick = { screen ->
                scope.launch { drawerState.close() }
                if (currentScreen != screen.route) {
                    currentScreen = screen.route
                }
            },
            onCloseClick = {
                scope.launch { drawerState.close() }
            }
        )

        BoxWithBackground(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .coloredShadow(
                    color = Color.Black,
                    alpha = 0.1f,
                    shadowRadius = 50.dp
                )
                .clickable(enabled = drawerState.isOpen) {
                    scope.launch { drawerState.close() }
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                MyTopAppBar(
                    navigationBar = {
                        IconButton(onClick = { scope.launch { drawerState.opposite() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = {
                        Text(
                            stringResource(R.string.myanmar_alphabet_phonics),
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actionBar = {
                        IconButton(onClick = {
                            navProvider.openSetting()
                        }) {
                            Icon(Icons.Default.Settings, contentDescription = "Setting")
                        }
                    },
                )

                AnimatedContent(
                    currentScreen,
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.Transparent) // keep transparent
                ) { screen ->
                    when (screen) {

                        Screen.VowelLetters.route -> {
                            VowelLetterScreen(navProvider = navProvider)
                        }

                        Screen.ConsonantLetters.route -> {
                            ConsonantLettersScreen(navProvider = navProvider)
                        }

                        Screen.Quiz.route -> {
                            ConsonantQuizScreen(
                                mode = QuizMode.Standard
                            )
                        }

                        Screen.MiniGame.route -> {
                            ConsonantQuizScreen(
                                mode = QuizMode.MiniGame
                            )
                        }

                        Screen.QuizDaily.route -> {
                            ConsonantQuizScreen(
                                mode = QuizMode.Daily
                            )
                        }

                        Screen.FeedBack.route -> {
                            FeedbackScreen()
                        }
                    }
                }
            }
        }
    }
}


suspend fun DrawerState.opposite() {
    if (this.isOpen) {
        this.close()
    } else {
        this.open()
    }
}