package com.tzh.mamp.ui.screen.setting

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.R
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.provider.ThemeProvider
import com.tzh.mamp.ui.component.BoxWithBackground
import com.tzh.mamp.ui.component.MyTopAppBar
import java.util.Locale

@SuppressLint("LocalContextConfigurationRead")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigationProvider: NavigationProvider
) {
    val context = LocalContext.current
    val theme by viewModel.themeFlow.collectAsState(initial = ThemeProvider.Theme.SYSTEM)
    val selectedLanguage by viewModel.languageFlow.collectAsState(initial = viewModel.getCurrentLanguage())

    val configuration = remember(selectedLanguage) {
        Configuration(context.resources.configuration).apply {
            setLocale(Locale(selectedLanguage))
        }
    }

    CompositionLocalProvider(LocalConfiguration provides configuration) {
        BoxWithBackground() {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                MyTopAppBar(navigationBar = {
                    IconButton(onClick = navigationProvider::onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }, title = {
                    Text(
                        stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                })
                Surface(
                    color = Color.Transparent,
                    contentColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text("Theme", style = MaterialTheme.typography.titleLarge)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = theme == ThemeProvider.Theme.LIGHT,

                                onClick = { viewModel.setTheme(ThemeProvider.Theme.LIGHT) }
                            )
                            Text("Light")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = theme == ThemeProvider.Theme.DARK,
                                onClick = { viewModel.setTheme(ThemeProvider.Theme.DARK) }
                            )
                            Text("Dark")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = theme == ThemeProvider.Theme.SYSTEM,
                                onClick = { viewModel.setTheme(ThemeProvider.Theme.SYSTEM) }
                            )
                            Text("System Default")
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text("Language", style = MaterialTheme.typography.titleLarge)

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedLanguage == "en",
                                onClick = {
                                    viewModel.setLanguage("en", context)
                                }
                            )
                            Text("English")
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedLanguage == "my",
                                onClick = {
                                    viewModel.setLanguage("my", context)
                                }
                            )
                            Text("မြန်မာ")
                        }
                    }
                }
            }
        }
    }
}


