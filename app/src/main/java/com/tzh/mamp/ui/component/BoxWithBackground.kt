package com.tzh.mamp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tzh.mamp.R

@Composable
fun BoxWithBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
    ) {

        GifBackground(
            R.drawable.background,
            modifier
                .fillMaxSize()
        )
        content()
    }
}