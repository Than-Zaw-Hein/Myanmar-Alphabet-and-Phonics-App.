package com.tzh.mamp.ui.screen.main

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.provider.LanguageProvider
import com.tzh.mamp.provider.ThemeProvider
import com.tzh.mamp.ui.navigation.navHost.MainRoot
import com.tzh.mamp.ui.theme.MAPATheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var themeProvider: ThemeProvider

    @Inject
    lateinit var appLanguageProvider: LanguageProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val bgPlayer = rememberBackgroundPlayer()
            LaunchedEffect(Unit) {
                bgPlayer.start()
            }

            DisposableEffect(Unit) {
                onDispose {
                    bgPlayer.stop()
                    bgPlayer.release()
                }
            }


            val theme by themeProvider.observeTheme().collectAsState(themeProvider.theme)
            val mode = when (theme) {
                ThemeProvider.Theme.LIGHT -> false
                ThemeProvider.Theme.DARK -> true
                ThemeProvider.Theme.SYSTEM -> isSystemInDarkTheme()
            }
            val language by appLanguageProvider.observeLanguageCode()
                .collectAsState(appLanguageProvider.getLanguageCode())
            CompositionLocalProvider(LocalLayoutDirection provides if (language == "my") LayoutDirection.Ltr else LayoutDirection.Ltr) {
                CompositionLocalProvider(LocalConfiguration provides this.resources.configuration.apply {
                    appLanguageProvider.setLocale(language, this@MainActivity)
                }) {
                    MAPATheme(darkTheme = mode) {
                        MainRoot(bgPlayer)
                    }
                }
            }
        }
    }
}

@Composable
fun rememberBackgroundPlayer(): MediaPlayer {
    val context = LocalContext.current
    return remember {
        MediaPlayer().apply {
            val afd = context.assets.openFd("background.mp3")
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            isLooping = true
            prepare()
        }
    }
}

@Composable
fun CustomSplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // simulate loading
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.mamp),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}
