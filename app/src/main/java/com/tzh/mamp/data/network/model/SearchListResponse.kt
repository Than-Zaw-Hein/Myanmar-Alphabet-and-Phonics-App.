package com.tzh.mamp.data.network.model

import com.squareup.moshi.Json

data class SearchListResponse(
    @Json(name = "items")  val items: List<SearchResultItem>
)


data class SearchResultItem(
    @Json(name = "id")   val id: SearchResultId,
    @Json(name = "snippet") val snippet: SearchResultSnippet
)

data class SearchResultId(
    @Json(name = "videoId")   val videoId: String
)

data class SearchResultSnippet(
    @Json(name = "title")  val title: String,
    @Json(name = "description")  val description: String
    // You can add more fields here if needed, like thumbnails
)