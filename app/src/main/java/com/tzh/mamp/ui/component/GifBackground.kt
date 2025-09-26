package com.tzh.mamp.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun GifBackground(gifRes: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader =
        ImageLoader.Builder(context)
            .components {
                // Support GIF decoding depending on Android version
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    AsyncImage(
        model = gifRes,
        contentDescription = null,
        imageLoader = imageLoader,
        modifier = modifier.fillMaxSize(),
        contentScale = if (isLandscape) ContentScale.FillWidth else ContentScale.FillHeight,
    )
}