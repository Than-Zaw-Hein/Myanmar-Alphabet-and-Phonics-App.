package com.tzh.mamp.data.network

import com.squareup.moshi.Json
data class PlaylistItemsResponse(
    @Json(name = "items") val items: List<PlaylistItemDetail>
)


data class PlaylistItemDetail(
    @Json(name = "snippet") val snippet: Snippet,
    @Json(name = "status")  val status: VideoStatus // Add this status field
)

data class ResourceId(
    @Json(name = "videoId") val videoId: String
)

data class Snippet(
    @Json(name = "title") val title: String,
    val description: String, // 'description' still matches, so no annotation needed
    @Json(name = "resourceId") val resourceId: ResourceId
)
data class VideoStatus(
    @Json(name = "privacyStatus")
    val privacyStatus: String // This will be "public", "private", or "unlisted"
)