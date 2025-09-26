package com.tzh.mamp.ui.screen.vowels

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.app.SoundPlayer
import com.tzh.mamp.data.model.VowelLetter
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.BannerAdView
import com.tzh.mamp.ui.component.VowelCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VowelLetterScreen(
    viewModel: VowelLetterViewModel = hiltViewModel(), navProvider: NavigationProvider
) {
    val vowels = viewModel.vowels
    var selectedVowel: VowelLetter? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 2,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            vowels.forEachIndexed { index, vowel ->
                VowelCard(
                    fontSize = 20.sp,
                    vowel = vowel,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(100.dp)
                        .padding(4.dp),// or any fixed size to keep layout consistent,
                    isBouncing = selectedVowel == vowel,
                    onClick = {
                        selectedVowel = if (vowel == selectedVowel) {
                            null
                        } else {
                            vowel
                        }
                        navProvider.openToAlphabet(vowel.id - 1, AlphabetType.VOWEL)
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
            adUnitId = BuildConfig.VOWEL_ADS_KEY
        )
    }
}