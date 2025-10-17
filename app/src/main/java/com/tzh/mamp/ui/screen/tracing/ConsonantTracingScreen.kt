package com.tzh.mamp.ui.screen.tracing

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.app.SoundPlayer
import com.tzh.mamp.app.createConsonantBitmap
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.BannerAdView
import com.tzh.mamp.ui.component.BoxWithBackground
import com.tzh.mamp.ui.component.MyTopAppBar
import com.tzh.mamp.ui.component.drawBox.DrawBox
import com.tzh.mamp.ui.component.drawBox.rememberDrawController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalAnimationApi::class, FlowPreview::class)
@Destination
@Composable
fun ConsonantTracingScreen(
    consonant: String = "က",
    lottieIntroRes: Int = R.raw.pen_draw,
    lottieRewardRes: Int = R.raw.reward,
    navigationProvider: NavigationProvider,
    viewModel: TracingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val controller = rememberDrawController()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val context = LocalContext.current
    var isDrawing by remember { mutableStateOf(false) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(1f, animationSpec = tween(1000))
    }

    val soundPlayer = remember {
        SoundPlayer(context)
    }
    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }
    val consonantBitmap = remember {
        createConsonantBitmap(screenWidth / 2, screenHeight / 2, consonant)
    }
    var latestCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(controller) {
        controller.setStrokeColor(Color.Yellow)
        controller.setStrokeWidth(10f)
    }

    LaunchedEffect(uiState.showFail) {
        if (uiState.showFail == true) {
            soundPlayer.playSound(R.raw.fail)
            delay(2000)
            controller.reset()
            latestCount = 0
            viewModel.onEvent(TracingUiEvent.HideFail)
        }
    }
    LaunchedEffect(uiState.showReward) {
        if (uiState.showReward) {
            soundPlayer.playSound(R.raw.winner_sound)
        }
    }
    // Check the latest drawing whenever undoCount changes
    LaunchedEffect(latestCount, isDrawing) {
        snapshotFlow { isDrawing }
            .filter { !it && latestCount > 0 } // Only when drawing stops AND there is something to check
            .debounce(500L) // Wait a moment before firing the check
            .collectLatest {
                viewModel.onEvent(
                    TracingUiEvent.CheckLatestDrawing(
                        getUserBitmap = { controller.getDrawBoxBitmap() },
                        consonantBitmap = consonantBitmap
                    )
                )
            }
    }

    BoxWithBackground(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        val canvasModifier = Modifier
            .width((screenWidth / 2).dp)
            .height((screenHeight / 2).dp)
        if (isLandscape) {
            // Landscape layout: side by side
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clickable(uiState.showIntro) {
                            viewModel.onEvent(TracingUiEvent.HideIntro)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Consonant background + drawing
                    Image(
                        bitmap = consonantBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = canvasModifier.graphicsLayer { this.alpha = alpha.value }

                    )
                    if (!uiState.showIntro && !uiState.showReward) {
                        DrawBox(
                            drawController = controller,
                            modifier = canvasModifier.background(Color.LightGray.copy(0.3f)),
                            onDraw = { isDrawing = true },
                            trackHistory = { undoCount, _ ->
                                latestCount = undoCount
                                isDrawing = false
                            })
                    }
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AnimatedText(uiState, consonant)
                    if (uiState.isChecking) {
                        CheckingIndicator()
                    }

                    if (uiState.showFail == true) {
                        ErrorMessage(stringResource(R.string.try_again))
                    }
                    // Banner ad at bottom

                    BannerAdView(
                        adUnitId = BuildConfig.TRACING_ADS_KEY
                    )
                }
            }
        } else {
            // Portrait layout: top-down
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(uiState.showIntro) {
                            viewModel.onEvent(TracingUiEvent.HideIntro)
                        }) {
                    Image(
                        bitmap = consonantBitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = canvasModifier.graphicsLayer { this.alpha = alpha.value }

                    )
                    if (!uiState.showIntro && !uiState.showReward) {
                        DrawBox(
                            drawController = controller,
                            modifier = canvasModifier.background(Color.LightGray.copy(0.3f)),
                            onDraw = { isDrawing = true },
                            trackHistory = { undoCount, _ ->
                                latestCount = undoCount
                                isDrawing = false
                            })
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                AnimatedText(uiState, consonant)
                if (uiState.isChecking) {
                    CheckingIndicator()
                }
                if (uiState.showFail == true) {
                    ErrorMessage(stringResource(R.string.try_again))
                }
                // Banner ad at bottom


                BannerAdView(
                    adUnitId = BuildConfig.TRACING_ADS_KEY
                )
            }
        }

        // Top AppBar
        Column(modifier = Modifier.fillMaxWidth()) {
            MyTopAppBar(
                navigationBar = {
                    IconButton(onClick = navigationProvider::onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        stringResource(R.string.tracing, consonant),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .weight(1f)
                    )
                },
                actionBar = {
                    TextButton(
                        onClick = {
                            viewModel.onEvent(TracingUiEvent.Reset)
                            controller.reset()
                            latestCount = 0
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "retry")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.retry),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                },
            )
        }
        // Intro / Reward animations
        if (uiState.showIntro) {
            TracingIntroAnimation(lottieRes = lottieIntroRes)
        } else if (uiState.showReward) {
            TracingIntroAnimation(lottieRes = lottieRewardRes)
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun AnimatedText(uiState: TracingUiState, consonant: String) {
    AnimatedContent(
        targetState = uiState.showReward,
        transitionSpec = {
            fadeIn(animationSpec = tween(500)).togetherWith(
                fadeOut(
                    animationSpec = tween(
                        500
                    )
                )
            )
        },
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) { showReward ->
        Text(
            text = if (showReward) stringResource(R.string.awesome_you_traced_it_perfectly)
            else stringResource(R.string.follow_along_to_trace_the_letter, consonant),
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White, textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CheckingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(color = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            stringResource(R.string.analyzing_your_strokes),
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White, textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
private fun ErrorMessage(msg: String) {
    Text(
        msg,
        color = Color.Red,
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        textAlign = TextAlign.Center
    )


}
