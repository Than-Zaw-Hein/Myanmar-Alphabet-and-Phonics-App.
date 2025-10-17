package com.tzh.mamp.data.network

import com.tzh.mamp.BuildConfig
import com.tzh.mamp.data.network.model.SearchListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApiService {

    @GET("playlistItems")
    suspend fun getPlaylistVideos(
        @Query("playlistId") playlistId: String,
        @Query("part") part: String = "snippet,status",
        @Query("maxResults") maxResults: Int = 25, // Get up to 20 videos
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): PlaylistItemsResponse

    @GET("search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("part") part: String = "snippet",
        @Query("maxResults") maxResults: Int = 20,
        @Query("type") type: String = "video", // Ensures we only get videos
        @Query("key") apiKey: String = BuildConfig.YOUTUBE_API_KEY
    ): SearchListResponse
}