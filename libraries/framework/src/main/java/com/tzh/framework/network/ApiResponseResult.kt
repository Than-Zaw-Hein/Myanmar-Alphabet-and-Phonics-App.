package com.tzh.framework.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@JsonClass(generateAdapter = true)
data class ApiResponseResult(

    @SerialName("errorMessages")
    @Json(name = "errorMessages")
    val errorMessages: Map<Int, String>? = null,


    @SerialName("statusCode")
    @Json(name = "statusCode")
    val statusCode: Int? = null
)