package com.tzh.mamp.ui.screen.consonant_letters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.AlphabetCard
import com.tzh.mamp.ui.component.BannerAdView

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ConsonantLettersScreen(
    viewModel: ConsonantLetterViewModel = hiltViewModel(), navProvider: NavigationProvider
) {
    val alphabets = viewModel.alphabets
    val context = LocalContext.current

    var selectedConsonant: Consonant? by remember { mutableStateOf(null) }

    BoxWithConstraints() {
        val sizeWidth = maxWidth / 6
        val dynamicFontSize = when {
            sizeWidth > 150.dp -> 34.sp
            sizeWidth > 100.dp -> 20.sp
            else -> 24.sp
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.Center,
                maxItemsInEachRow = 5,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                alphabets.forEachIndexed { index, alphabet ->

                    val color = remember {
                        if (index < 10) {
                            Color.Red
                        } else if (index < 20) {
                            Color.Magenta
                        } else {
                            Color.Blue
                        }
                    }

                    AlphabetCard(
                        color = color,
                        fontSize = dynamicFontSize,
                        consonant = alphabet,
                        modifier = Modifier
                            .width(sizeWidth)
                            .padding(4.dp),// or any fixed size to keep layout consistent,
                        isBouncing = selectedConsonant == alphabet,
                        onClick = {
                            selectedConsonant = if (alphabet == selectedConsonant) {
                                null
                            } else {
                                alphabet
                            }
                            navProvider.openToAlphabet(alphabet.id - 1, AlphabetType.CONSONANT)
                        })
                }
            }
            Button(
                onClick = {
                    navProvider.openVideoPlayer()
                },
            ) {
                Icon(Icons.Default.PlayCircleOutline, contentDescription = "Play Consonant Letters")
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.play_consonant_letters))
            }

            // Banner ad at bottom

            BannerAdView(
                adUnitId = BuildConfig.HOMESCREEN_ADS_KEY
            )
        }
    }
}