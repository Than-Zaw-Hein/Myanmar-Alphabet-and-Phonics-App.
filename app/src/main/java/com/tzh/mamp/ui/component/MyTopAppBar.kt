package com.tzh.mamp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable

fun MyTopAppBar(
    navigationBar: @Composable RowScope.() -> Unit = {},
    title: @Composable RowScope.() -> Unit = {},
    actionBar: @Composable RowScope.() -> Unit = {},

    ) {


    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        contentColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationBar()
            title()
            actionBar()
        }
    }

}