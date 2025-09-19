package com.tzh.mamp.app

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@OptIn(UnstableApi::class)
object MyPlayer {


    fun providePlayer(application: Context): ExoPlayer {
        val trackSelector = DefaultTrackSelector(application)
        val loadControl = DefaultLoadControl()
        val renderersFactory = DefaultRenderersFactory(application).setEnableDecoderFallback(true)
        // Specify the video renderer index (0 is assumed for video)
        val videoRendererIndex = 0

        // Specify the desired video resolution
        val targetWidth = 1920
        val targetHeight = 1080

        // Set up track selection parameters
        val trackSelectionParameters = DefaultTrackSelector.ParametersBuilder(application)
            .setMaxVideoSizeSd()
            .build()

        // Apply the track selection parameters to the track selector
        trackSelector.parameters = trackSelectionParameters
        return ExoPlayer.Builder(application)
            .setTrackSelector(trackSelector)
            .setLoadControl(loadControl)
            .setRenderersFactory(renderersFactory)
            .setSeekBackIncrementMs(10000)     // 10 seconds
            .setSeekForwardIncrementMs(10000)  // 10 seconds
            .build().apply {
                setHandleAudioBecomingNoisy(true)
                playWhenReady = false
            }
    }
}