package com.tzh.mamp.ui.screen.alphabet

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.app.AlphabetType.CONSONANT
import com.tzh.mamp.app.AlphabetType.VOWEL
import com.tzh.mamp.app.SoundPlayer
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.VowelLetter
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.BannerAdView
import com.tzh.mamp.ui.component.BoxWithBackground
import com.tzh.mamp.ui.component.LetterContent
import com.tzh.mamp.ui.component.LetterContentList
import com.tzh.mamp.ui.component.MyTopAppBar

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

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer.release()
        }
    }

    val activity = context as? Activity
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val pagerState = rememberPagerState(initialPage = index, pageCount = { alphabets.size })

    BoxWithBackground(
        modifier = Modifier.systemBarsPadding()
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            MyTopAppBar(
                navigationBar = {
                    IconButton(onClick = navigationProvider::onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {
                    Text(
                        text = when (type) {
                            CONSONANT -> stringResource(R.string.swipe_to_learn_consonant)
                            VOWEL -> stringResource(R.string.swipe_to_learn_vowel)
                        },
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            )
            Surface(
                color = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

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
                                    .padding(16.dp),
                                isSoundPlaying = soundPlayer.isPlaying,
                                type = type,
                                letter = when (type) {
                                    CONSONANT -> (item as Consonant).letter
                                    VOWEL -> (item as VowelLetter).symbol
                                },
                                onPlaySound = {
                                    when (type) {
                                        CONSONANT -> {
                                            val inputStream =
                                                context.assets.open((item as Consonant).filePath)
                                            soundPlayer.playFromInputStream(inputStream)
                                        }

                                        VOWEL -> {
                                            val inputStream =
                                                context.assets.open((item as VowelLetter).filePath)
                                            soundPlayer.playFromInputStream(inputStream)
                                        }
                                    }
                                },
                                onTracing = {
                                    navigationProvider.openTracing((item as Consonant).letter)
                                },
                                getContentTextColor = {
                                    if (index < 10) {
                                        Color.Red
                                    } else if (index < 20) {
                                        Color.Magenta
                                    } else {
                                        Color.Blue
                                    }
                                }
                            )
                        }
                    )
                    BannerAdView(
                        adUnitId = BuildConfig.DETAIL_ADS_KEY
                    )
                }
            }
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
