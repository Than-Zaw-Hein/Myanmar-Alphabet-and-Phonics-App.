package com.tzh.mamp.ui.screen.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tzh.mamp.R

@Composable
fun FeedbackScreen(
    onSubmit: (String) -> Unit = {}
) {
    var feedbackText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.feedback_message),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Yellow,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(R.string.request_feedback_message),
            fontSize = 16.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        OutlinedTextField(
            value = feedbackText,
            onValueChange = { feedbackText = it },
            label = { Text(stringResource(R.string.your_suggestion)) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            singleLine = false,
            maxLines = 6,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedBorderColor = Color.White,
                focusedLabelColor = Color.White,
                disabledTextColor = Color.White,
                disabledLabelColor = Color.White,
            )

        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSubmit(feedbackText)
                feedbackText = ""
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}
