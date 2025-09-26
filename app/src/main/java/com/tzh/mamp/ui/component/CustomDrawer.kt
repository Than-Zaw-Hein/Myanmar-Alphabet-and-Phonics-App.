package com.tzh.mamp.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.tzh.mamp.R
import com.tzh.mamp.ui.navigation.route.Screen


val backGroundBrush = Brush.sweepGradient(
    colors = listOf(
        Color.White,
        Color.Blue,
        Color.White,
        Color.Blue,
        Color.White,
        Color.Blue,
        Color.White,
        Color.Blue,
        Color.White,
        Color.Blue,
    )
)

@Composable
fun CustomDrawer(
    isOpenDrawer: Boolean,
    screenDrawers: List<Screen>,
    selectedScreenDrawers: String,
    onNavigationItemClick: (Screen) -> Unit,
    onCloseClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotationAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )
    val animateAlpha by animateFloatAsState(
        targetValue = if (isOpenDrawer) 1f else 0f,
        )

    BoxWithBackground(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxHeight()
            .fillMaxWidth(fraction = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .alpha(animateAlpha)
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow Icon",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .drawBehind {
                        val padding = 16.dp.toPx() // your desired padding
                        val radius = (size.minDimension / 2)
                        rotate(rotationAnimation) {
                            drawCircle(
                                brush = backGroundBrush,
                                radius = radius,
                                center = Offset(
                                    size.width / 2,
                                    size.height / 2
                                ), // center of box
                                style = Stroke(width = 4f)
                            )
                        }
                    }
            )
            Text(
                "Myanmar Alphabet And Phonics",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))

            screenDrawers.forEach { screen ->
                NavigationDrawerItem(
                    shape = RoundedCornerShape(4.dp),
                    label = { Text(stringResource(screen.title)) },
                    icon = {
                        Icon(
                            screen.icon, contentDescription = stringResource(screen.title)
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White
                    ),
                    selected = screen.route == selectedScreenDrawers,
                    onClick = { onNavigationItemClick(screen) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }

        }
    }
}