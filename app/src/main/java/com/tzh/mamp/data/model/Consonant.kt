package com.tzh.mamp.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Consonant(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("letter")
    val letter: String = "",
    @SerialName("phonetic")
    val phonetic: String = "",
    @SerialName("example")
    val example: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("file_path")
    val filePath: String = "consonant/မြန်မာအက္ခရာAlphabet.mp3"
)