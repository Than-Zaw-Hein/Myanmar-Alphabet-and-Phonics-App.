package com.tzh.mamp.data.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class VowelLetter(
    @SerialName("id")
    val id: Int,
    @SerialName("symbol")
    val symbol: String,
    @SerialName("sound")
    val sound: String,
    @SerialName("example")
    val example: String,
    @SerialName("equivalent")
    val equivalent: String,
    @SerialName("fileLink")
    val fileLink: String
)