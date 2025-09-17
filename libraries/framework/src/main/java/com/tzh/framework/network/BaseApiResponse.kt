package com.tzh.framework.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface BaseApiResponse {

    @SerialName("result")
    @Json(name = "result")
    val apiResponseResult: ApiResponseResult?
}

@Serializable
@JsonClass(generateAdapter = true)
data class BaseResponse(
    @SerialName("result")
    @Json(name = "result")
    override val apiResponseResult: ApiResponseResult? = null,
) : BaseApiResponse