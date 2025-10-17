// Create a new file in /ui/screen/video/VideoPlayerScreen.kt
package com.tzh.mamp.ui.screen.video

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.framework.extension.getActivity
import com.tzh.jetframework.ControlSystemStatusBar
import com.tzh.mamp.R
import com.tzh.mamp.app.Extension.setScreenOrientation
import com.tzh.mamp.provider.NavigationProvider
import com.tzh.mamp.ui.component.BoxWithBackground
import com.tzh.mamp.ui.component.CustomTopBar

@Composable
@Destination
fun VideoPlayerScreen(
    navigationProvider: NavigationProvider,
    videoId: String,
    title: String,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var playbackPosition by rememberSaveable { mutableFloatStateOf(0f) }
    val context = LocalContext.current
    val activity = context.getActivity()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE


    // Control system UI
    ControlSystemStatusBar(isLandscape, activity)
    var playState by remember { mutableStateOf(PlayerConstants.PlayerState.UNKNOWN) }

    // Helper function to manage screen orientation
    fun toggleScreenOrientation() {
        val newOrientation = if (isLandscape) {
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        }
        activity?.requestedOrientation = newOrientation
    }
    BoxWithBackground(
        modifier = Modifier.systemBarsPadding()
    ) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center)
                .then(
                    if (isLandscape) Modifier.fillMaxSize()
                    else Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                ),
            factory = { ctx ->
                val view = YouTubePlayerView(ctx).apply {
                    lifecycleOwner.lifecycle.addObserver(this)
                    val listener = object : AbstractYouTubePlayerListener() {
                        @SuppressLint("UseCompatLoadingForDrawables")
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            val defaultUi =
                                DefaultPlayerUiController(this@apply, youTubePlayer).apply {
                                    // Group all UI element visibility settings
                                    showYouTubeButton(true)
                                    showVideoTitle(false)
                                    showFullscreenButton(true)
                                    showSeekBar(true)
                                    showPlayPauseButton(true)
                                    showCustomAction1(true)
                                    showCustomAction2(true)

                                    // Set up custom actions using resource IDs for icons
                                    // This avoids the need for a Context instance and the !! operator.
                                    setCustomAction1(ctx.getDrawable(R.drawable.back)!!) {
                                        youTubePlayer.seekTo(playbackPosition - 10)
                                    }

                                    setCustomAction2(ctx.getDrawable(R.drawable.forward)!!) {
                                        youTubePlayer.seekTo(playbackPosition + 10)
                                    }
                                    // Set the fullscreen click listener to call the dedicated function
                                    setFullscreenButtonClickListener { toggleScreenOrientation() }
                                }
                            setCustomPlayerUi(defaultUi.rootView)

                            youTubePlayer.loadOrCueVideo(
                                lifecycleOwner.lifecycle,
                                videoId,
                                playbackPosition
                            )
                        }

                        override fun onCurrentSecond(player: YouTubePlayer, second: Float) {
                            playbackPosition = second
                        }

                        override fun onStateChange(
                            youTubePlayer: YouTubePlayer,
                            state: PlayerConstants.PlayerState
                        ) {
                            playState = state
                        }
                    }
                    val options = IFramePlayerOptions.Builder().controls(0).build()
                    enableAutomaticInitialization = false
                    initialize(listener, true, options)
                }
                view
            }
        )

        // Show top bar only in portrait
        CustomTopBar(
            isVisible = playState != PlayerConstants.PlayerState.PLAYING,
            title = title,
            dominantColor = Color.White,
            showDownload = false,
            download = {},
            onBackPress = { navigationProvider.onBack() },
        )
    }

    // Handle orientation and cleanup
    DisposableEffect(Unit) {
        activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE)
        onDispose {
            activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT)
        }
    }
}
