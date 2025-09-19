package com.tzh.mamp.ui.component.flip

sealed class FlipPagerOrientation {
    data object Vertical : FlipPagerOrientation()
    data object Horizontal : FlipPagerOrientation()
}