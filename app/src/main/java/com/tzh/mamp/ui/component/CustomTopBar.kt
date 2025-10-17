package com.tzh.mamp.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopBar(
    title: String? = null,
    isVisible: Boolean = true,
    dominantColor: Color,
    showDownload: Boolean = true,
    onBackPress: () -> Unit,
    download: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackPress) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = dominantColor
                    )
                }
                title?.let {
                    Text(it, style = MaterialTheme.typography.titleLarge.copy(Color.White))
                }
            }
            if (showDownload) {
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = download, Modifier.padding(end = 36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Download",
                        tint = dominantColor
                    )
                }
            }
//            AndroidView(
//                factory = { context ->
//
//                    val button = CustomMediaRouteButton(context)
//                    // Lazy load Google Cast context. It needs to be done here where we have a Context,
//                    // otherwise we won't have a CastContext "later" in the application
//                    CastButtonFactory.setUpMediaRouteButton(context.applicationContext, button)
//                    button
//                },
//                onRelease = { MediaRouteButtonManager.remove(it) },
//                update = { updatedButton: CustomMediaRouteButton ->
////                    if (updatedButton.isAttachedToWindow) {
////                        MediaRouteButtonManager.add(updatedButton)
////                    }
//                },
//                onReset = {}, // The necessary thing here is to have a non-null argument
//                modifier = Modifier.padding(end = 36.dp)
//            )
        }
    }
}
