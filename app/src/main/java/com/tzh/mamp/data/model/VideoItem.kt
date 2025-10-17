package com.tzh.mamp.data.model

data class VideoItem(
    val id: String = "",
    val title: String = "",
    val videoId: String = "",
    val description: String = "",
    // The category can be assigned in the ViewModel based on which playlist was fetched
    val category: String = ""
)

// Helper function to create thumbnail URL
fun VideoItem.getThumbnailUrl(): String {
    return "https://img.youtube.com/vi/$videoId/0.jpg"
}