package com.tzh.mamp.ui.component


import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.tzh.mamp.ui.component.flip.FlipPager
import com.tzh.mamp.ui.component.flip.FlipPagerOrientation
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
inline fun <reified T : Any> LetterContentList(
    modifier: Modifier = Modifier,
    list: List<T>,
    pagerState: PagerState,
    crossinline content: @Composable (T, Int) -> Unit,

    ) {
    val isEmulator = Build.FINGERPRINT.contains("generic")
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if (!isEmulator) {
        HorizontalPager(state = pagerState) { page ->
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
            val absoluteOffset = pageOffset.absoluteValue
            val rotationY = lerp(
                start = 0f,
                stop = 40f,
                fraction = pageOffset.coerceIn(-1f, 1f)
            )
            LaunchedEffect(page) {
                listState.animateScrollToItem(
                    pagerState.currentPage,
                    scrollOffset = pagerState.currentPage -1
                )
            }
            content(list[page], page)
        }
    } else {
        FlipPager(
            state = pagerState,
            orientation = FlipPagerOrientation.Horizontal,
            modifier = modifier
        ) { page ->
            LaunchedEffect(page) {
                listState.animateScrollToItem(
                    pagerState.currentPage,
                    scrollOffset = pagerState.currentPage -1
                )
            }
            val alphabet = list[page]
            content(list[page], page)

        }
    }
    Spacer(Modifier.height(24.dp))
    // Pager Indicator


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage - 1,
                    )

                }
            },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                "play ${pagerState.currentPage} Back"
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f), state = listState,
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {

            items(list.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                        .clickable(
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                        )
                )
            }
        }

        IconButton(
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagerState.currentPage + 1,
                    )
                }
            },
            modifier = Modifier
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                "play ${pagerState.currentPage} Forward"
            )
        }
    }

}
