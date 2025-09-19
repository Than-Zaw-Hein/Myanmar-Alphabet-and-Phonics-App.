package com.tzh.mamp.ui.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.ui.PlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.jetframework.ControlSystemStatusBar
import com.tzh.mamp.R
import com.tzh.mamp.app.Extension.setScreenOrientation
import com.tzh.mamp.app.MyPlayer
import androidx.core.net.toUri
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import com.tzh.mamp.provider.NavigationProvider

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun VideoPlayer(
    navigationProvider: NavigationProvider
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var visibleTopAppBar by rememberSaveable {
        mutableStateOf(false)
    }
    val player = remember {
        MyPlayer.providePlayer(context).apply {
            val videoUri =
                "android.resource://${context.packageName}/${R.raw.myanmar_consonants_burmese_alphabet}".toUri()
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    // Optional: handle play/pause state
                }

                override fun onPlayerError(error: PlaybackException) {
                    Toast.makeText(context, "Playback error: ${error.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    // System UI control
    ControlSystemStatusBar(isLandscape, activity)
    Scaffold(
        topBar = {
            AnimatedVisibility(visibleTopAppBar) {
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
        }
    ) { innerPadding ->
        // Video player view
        AndroidView(
            factory = { viewContext ->
                PlayerView(viewContext).apply {
                    this.player = player
                    setUseController(true)
                    setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
                        visibleTopAppBar = visibility == View.VISIBLE
                    })
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }

    // Handle orientation and cleanup
    DisposableEffect(Unit) {
        activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE)
        onDispose {
            activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)
            player.release()
        }
    }
}