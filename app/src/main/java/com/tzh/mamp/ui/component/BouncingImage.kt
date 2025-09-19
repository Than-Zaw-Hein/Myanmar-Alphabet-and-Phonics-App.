package com.tzh.mamp.ui.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun BouncingContent(
    content: @Composable (modifier: Modifier) -> Unit,
    isAnimated: Boolean,
) {
    val scale = if (isAnimated) {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        infiniteTransition.animateFloat(
            initialValue = 0.9f, targetValue = 2f, animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        ).value
    } else {
        1f // No animation, so we stay at the default scale
    }

    content(Modifier.scale(scale))
}
