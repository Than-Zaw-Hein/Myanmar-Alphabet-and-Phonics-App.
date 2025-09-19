package com.tzh.mamp.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tzh.mamp.data.model.VowelLetter

@Composable
fun VowelCard(
    fontSize: TextUnit,
    modifier: Modifier,
    vowel: VowelLetter,
    isBouncing: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            BouncingContent(
                content = { modifier ->
                    Text(
                        modifier = modifier.padding(16.dp),
                        text = vowel.symbol,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = fontSize
                        ),
                    )
                }, isBouncing
            )
        }
    }
}