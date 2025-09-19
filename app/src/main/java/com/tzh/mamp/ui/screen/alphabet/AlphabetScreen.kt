package com.tzh.mamp.ui.screen.alphabet

import android.app.Activity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.R
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.app.AlphabetType.CONSONANT
import com.tzh.mamp.app.AlphabetType.VOWEL
import com.tzh.mamp.app.SoundPlayer
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.VowelLetter
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.LetterContent
import com.tzh.mamp.ui.component.LetterContentList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Destination
@Composable
fun AlphabetScreen(
    navigationProvider: NavigationProvider,
    index: Int,
    type: AlphabetType,
    viewModel: AlphabetViewModel = hiltViewModel()
) {
    val alphabets = when (type) {
        CONSONANT -> viewModel.consonent
        VOWEL -> viewModel.vowelLetter
    }
    val context = LocalContext.current

    val soundPlayer = remember {
        SoundPlayer(context)
    }
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )
    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState(initialPage = index, pageCount = { alphabets.size })

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = navigationProvider::onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when (type) {
                    CONSONANT -> stringResource(R.string.swipe_to_learn_consonant)
                    VOWEL -> stringResource(R.string.swipe_to_learn_vowel)
                },
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))

            LetterContentList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                pagerState = pagerState,
                list = alphabets,
                content = { item, page ->
                    LaunchedEffect(page) {
                        soundPlayer.release()
                    }

                    LetterContent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .drawBehind {
                                if (soundPlayer.isPlaying) {
                                    val padding = 16.dp.toPx() // your desired padding
                                    val radius = (size.minDimension / 2) - padding
                                    rotate(rotationAnimation) {
                                        drawCircle(
                                            brush = rainbowBrush,
                                            radius = radius,
                                            center = Offset(size.width / 2, size.height / 2), // center of box
                                            style = Stroke(width = 10f)
                                        )
                                    }
                                }
                            },
                        letter = when (type) {
                            CONSONANT -> (item as Consonant).letter
                            VOWEL -> (item as VowelLetter).symbol
                        },
                        onPlaySound = {
                            when (type) {
                                CONSONANT -> {
                                    (item as Consonant).phonetic
                                }

                                VOWEL -> {
                                    val inputStream =
                                        context.assets.open((item as VowelLetter).fileLink)
                                    soundPlayer.playFromInputStream(inputStream)
                                }
                            }
                        },
                    )
                }
            )
        }
    }
}

val rainbowBrush = Brush.sweepGradient(
    colors = listOf(
        Color.Red,
        Color.Magenta,
        Color.Blue,
        Color.Cyan,
        Color.Green,
        Color.Yellow,
        Color.Red // loop back for smooth transition
    )
)
