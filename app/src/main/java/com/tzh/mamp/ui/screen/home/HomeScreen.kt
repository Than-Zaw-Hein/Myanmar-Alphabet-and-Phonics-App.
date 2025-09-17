package com.tzh.mamp.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.provider.NavigationProvider

@Composable
fun HomeScreen(
    navProvider: NavigationProvider
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to Myanmar Alphabet & Phonics!", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                navProvider.openVideoPlyaer()
            },
        ) {
            Text("Start Learning Alphabet")
        }
    }
}