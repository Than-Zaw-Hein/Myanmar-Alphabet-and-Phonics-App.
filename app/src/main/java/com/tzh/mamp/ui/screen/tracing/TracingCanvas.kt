package com.tzh.mamp.ui.screen.tracing

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.tzh.mamp.ui.component.drawBox.DrawBox
import com.tzh.mamp.ui.component.drawBox.PathWrapper
import com.tzh.mamp.ui.component.drawBox.rememberDrawController

@Composable
fun TracingCanvas(
    consonant: String,
    modifier: Modifier = Modifier,
    viewModel: TracingViewModel,
    onCompleted: () -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background faded consonant
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    consonant, size.width / 2, size.height / 2, android.graphics.Paint().apply {
                        color = android.graphics.Color.BLUE
                        textAlign = android.graphics.Paint.Align.CENTER
                        textSize = size.minDimension / 1.5f
                    })
            }
        }

    }
}
