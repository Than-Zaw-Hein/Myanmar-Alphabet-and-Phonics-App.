package com.tzh.mamp.ui.navigation.route

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.ui.graphics.vector.ImageVector
import com.tzh.mamp.R

sealed class Screen(val route: String, @StringRes val title: Int, val icon: ImageVector) {
    object ConsonantLetters : Screen(
        "consonant_letters",
        R.string.consonant_letters, Icons.Default.Home
    )

    object VowelLetters : Screen(
        "vowel_letters",
        R.string.vowel_letters, Icons.Default.Home
    )

    object Tracing : Screen("tracing", R.string.tracing, Icons.Default.Create)
    object Quiz : Screen("quiz", R.string.quiz, Icons.Default.QuestionAnswer)
    object FeedBack : Screen("feedback", R.string.feedback, Icons.Default.Feedback)

    companion object {
        val drawer = listOf(ConsonantLetters, VowelLetters, Quiz,FeedBack)
    }
}