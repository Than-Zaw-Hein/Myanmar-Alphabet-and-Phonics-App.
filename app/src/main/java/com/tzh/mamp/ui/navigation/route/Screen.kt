package com.tzh.mamp.ui.navigation.route

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Alphabet : Screen("alphabet", "Alphabet", Icons.Default.TextFields)
    object Tracing : Screen("tracing", "Tracing", Icons.Default.Create)
    object Quiz : Screen("quiz", "Quiz", Icons.Default.QuestionAnswer)


    companion object {
        val drawer = listOf(Home, Alphabet, Tracing, Quiz)

    }
}