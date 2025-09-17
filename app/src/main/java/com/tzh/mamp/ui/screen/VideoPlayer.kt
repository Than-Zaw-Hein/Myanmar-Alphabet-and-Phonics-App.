package com.tzh.mamp.ui.screen

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.jetframework.ControlSystemStatusBar
import com.tzh.mamp.R
import com.tzh.mamp.app.Extension.setScreenOrientation
import com.tzh.mamp.app.MyPlayer
import androidx.core.net.toUri
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import kotlinx.coroutines.flow.update

@Destination
@Composable
fun VideoPlayer() {
    val context = LocalContext.current
    val activity = context as? Activity
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {

        }

        override fun onPlayerError(error: PlaybackException) {
            Toast.makeText(
                context,
                "Playback error: ${error.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    DisposableEffect(Unit) {
        // Lock to landscape when entering
        activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        onDispose {
            // Restore to portrait when leaving
//            activity?.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }
    ControlSystemStatusBar(isLandscape, activity)
    val listener: PlayerView.ControllerVisibilityListener =
        PlayerView.ControllerVisibilityListener { visibility ->
//            isVisibleScreen = visibility == View.VISIBLE
//            onVisibility?.invoke(visibility)
        }
    val player = remember {
        MyPlayer.providePlayer(context).apply {
            addListener(playerListener)
            val uri =
                "android.resource://${context.packageName}/${R.raw.myanmar_consonants_burmese_alphabet}".toUri()
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            prepare()
        }
    }
    AndroidView(
        factory = {
            PlayerView(it).apply {

                this.player = player
                setControllerVisibilityListener(listener)
            }
        }, modifier = Modifier.fillMaxSize()
    )
}