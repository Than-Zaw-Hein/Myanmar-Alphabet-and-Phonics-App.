package com.tzh.mamp.ui.screen.main

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.tzh.mamp.R
import com.tzh.mamp.provider.ThemeProvider
import com.tzh.mamp.ui.navigation.navHost.MainRoot
import com.tzh.mamp.ui.theme.MAPATheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var themeProvider: ThemeProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            val theme by themeProvider.observeTheme().collectAsState(themeProvider.theme)
            val mode = when (theme) {
                ThemeProvider.Theme.LIGHT -> false
                ThemeProvider.Theme.DARK -> true
                ThemeProvider.Theme.SYSTEM -> isSystemInDarkTheme()
            }

            MAPATheme(darkTheme = mode) {
                MainRoot()
            }
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
