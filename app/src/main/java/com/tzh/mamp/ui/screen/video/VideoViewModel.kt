// Create a new file in /ui/screen/video/VideoViewModel.kt
package com.tzh.mamp.ui.screen.video

import androidx.compose.ui.geometry.isEmpty
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.mamp.data.model.VideoItem
import com.tzh.mamp.data.network.model.VideoCategory
import com.tzh.mamp.data.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
// A new data class to hold a category and its fetched videos together
data class CategoryWithVideos(
    val category: VideoCategory,
    val videos: List<VideoItem> = emptyList()
)

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {

    private val _stories = MutableStateFlow<List<VideoItem>>(emptyList())
    val stories = _stories.asStateFlow()

    private val _cartoons = MutableStateFlow<List<VideoItem>>(emptyList())
    val cartoons = _cartoons.asStateFlow()

    private val _knowledge = MutableStateFlow<List<VideoItem>>(emptyList())
    val knowledge = _knowledge.asStateFlow()
    private val _searchResults = MutableStateFlow<List<VideoItem>>(emptyList())
    val searchResults = _searchResults.asStateFlow()
    // 1. Add a SharedFlow for one-time events like showing a snackbar
    private val _snackbarEvent = MutableSharedFlow<String>()
    val snackbarEvent = _snackbarEvent.asSharedFlow()
    // A single state to hold all categories and their videos
    private val _videoCategories = MutableStateFlow<List<CategoryWithVideos>>(emptyList())
    val videoCategories = _videoCategories.asStateFlow()

    init {

            // Fetch categories from Firebase
            viewModelScope.launch {
                videoRepository.getVideoCategories().onEach { categories ->
                    // For each category fetched from Firebase, fetch its videos from YouTube
                    val categoriesWithVideos = categories.map { category ->
                        var videos: List<VideoItem> = emptyList()
                        // Fetch videos for this playlistId
                        videoRepository.getVideosFromPlaylist(category.playlistId).collect {
                            videos = it
                        }
                        CategoryWithVideos(category, videos)
                    }
                    _videoCategories.value = categoriesWithVideos
                }.launchIn(viewModelScope)
            }

    }


    private fun fetchVideos(playlistId: String, category: VideoCategory) {
        viewModelScope.launch {
            videoRepository.getVideosFromPlaylist(playlistId).collect { videoList ->
                when (category) {
                    VideoCategory.STORIES -> _stories.value = videoList
                    VideoCategory.CARTOONS -> _cartoons.value = videoList
                    VideoCategory.KNOWLEDGE -> _knowledge.value = videoList
                }
            }
        }
    }

    fun searchVideos(query: String) {
        viewModelScope.launch {
            videoRepository.searchVideosByName(query).collect {videoList ->
                _searchResults.value = videoList
                if (videoList.isEmpty()) {
                    _snackbarEvent.emit("No videos found for '$query'. Please try another search.")
                }
            }
        }
    }

    fun clearSearchResults() {
        _searchResults.value = emptyList()
    }
    // Enum to make category handling more robust and readable
    private enum class VideoCategory {
        STORIES,
        CARTOONS,
        KNOWLEDGE
    }
}
