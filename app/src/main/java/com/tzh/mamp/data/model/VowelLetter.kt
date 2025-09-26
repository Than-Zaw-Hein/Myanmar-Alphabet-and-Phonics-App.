package com.tzh.mamp.data.model

import androidx.annotation.Keep
import com.google.firebase.encoders.annotations.Encodable
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
    @get:Encodable.Ignore
    @SerialName("filePath")
    val filePath: String
)