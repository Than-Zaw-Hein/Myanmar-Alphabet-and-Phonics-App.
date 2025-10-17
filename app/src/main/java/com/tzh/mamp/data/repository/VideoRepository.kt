// Create a new file in /data/repository/VideoRepository.kt
package com.tzh.mamp.data.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.tzh.mamp.data.model.VideoItem
import com.tzh.mamp.data.network.YoutubeApiService
import com.tzh.mamp.data.network.model.VideoCategory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepository @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val youtubeApiService: YoutubeApiService
) {
    /**
     * Fetches the list of video categories (e.g., Stories, Cartoons) from Firebase.
     */
    fun getVideoCategories(): Flow<List<VideoCategory>> = callbackFlow {
        val reference = firebaseDatabase.getReference("video_categories")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = snapshot.children.mapNotNull { it.getValue<VideoCategory>() }
                trySend(categories).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                // On error, log the exception and then close the flow
                Timber.e(error.toException(), "Firebase onCancelled: Failed to read video categories.")
                close(error.toException())
            }
        }
        reference.addValueEventListener(listener)

        // Remove the listener when the channel is closed
        awaitClose { reference.removeEventListener(listener) }
    }


    /**
     * Fetches a list of videos from a specified YouTube playlist.
     *
     * @param playlistId The ID of the YouTube playlist to fetch.
     * @return A Flow that emits a list of VideoItem objects.
     */
    fun getVideosFromPlaylist(playlistId: String): Flow<List<VideoItem>> =
        flow {
            try {
                // Call the YouTube API to get playlist items
                val response = youtubeApiService.getPlaylistVideos(playlistId)

                // Map the network response to the app's internal VideoItem model
                val videoItems = response.items.mapNotNull { playlistItem ->
                    // *** ADD THIS CHECK ***
                    // Only include the video if its privacy status is "public"
                    if (playlistItem.status.privacyStatus == "public") {
                        playlistItem.snippet.resourceId.videoId.takeIf { it.isNotBlank() }?.let { videoId ->
                            VideoItem(
                                id = videoId,
                                videoId = videoId,
                                title = playlistItem.snippet.title,
                                description = playlistItem.snippet.description,
                                category = "" // This can be assigned in the ViewModel
                            )
                        }
                    } else {
                        null // Exclude non-public videos
                    }
                }
                emit(videoItems)

            } catch (e: Exception) {
                // In case of a network error or API issue, print the error and emit an empty list.
                e.printStackTrace()
                emit(emptyList())
            }
    }

    // In VideoRepository.kt

    fun searchVideosByName(query: String): Flow<List<VideoItem>> = flow {
        try {        val response = youtubeApiService.searchVideos(query)
            val videoItems = response.items.map { searchResult ->
                VideoItem(
                    id = searchResult.id.videoId,
                    videoId = searchResult.id.videoId,
                    title = searchResult.snippet.title,
                    description = searchResult.snippet.description,
                    category = "Search" // Or any other category
                )
            }
            emit(videoItems)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList())
        }
    }
}
