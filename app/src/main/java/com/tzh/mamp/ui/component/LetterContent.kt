package com.tzh.mamp.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tzh.mamp.app.AlphabetType
import com.tzh.mamp.ui.screen.alphabet.rainbowBrush

@Composable
fun LetterContent(
    modifier: Modifier = Modifier,
    type: AlphabetType,
    letter: String,
    isSoundPlaying: Boolean,
    onPlaySound: () -> Unit,
    onTracing: () -> Unit = {},
    getContentTextColor: () -> Color
) {


    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    if (isSoundPlaying) {
                        val padding = 16.dp.toPx() // your desired padding
                        val radius = (size.minDimension / 2) - padding
                        rotate(rotationAnimation) {
                            drawCircle(
                                brush = rainbowBrush,
                                radius = radius,
                                center = Offset(
                                    size.width / 2,
                                    size.height / 2
                                ), // center of box
                                style = Stroke(width = 10f)
                            )
                        }
                    }

                },
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = letter,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = getContentTextColor()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (type == AlphabetType.CONSONANT) {

                    IconButton(
                        onClick = onTracing,
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            "tracing $letter"
                        )
                    }
                }

                IconButton(
                    onClick = onPlaySound,
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayCircleFilled,
                        "play $letter"
                    )
                }
            }


        }
    }
}
