// Create a new file in /ui/screen/video/VideoScreen.kt
package com.tzh.mamp.ui.screen.video

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import com.tzh.mamp.data.model.VideoItem
import com.tzh.mamp.data.model.getThumbnailUrl
import com.tzh.mamp.provider.NavigationProvider
import kotlinx.coroutines.launch

@Composable
@Destination
fun VideoScreen(
    navigationProvider: NavigationProvider,
    viewModel: VideoViewModel = hiltViewModel()
) {
    // ... existing states
    val searchResults by viewModel.searchResults.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

// Get the new state from the ViewModel
    val categoriesWithVideos by viewModel.videoCategories.collectAsState()

    // --- State for the Snackbar ---
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // --- Observer for Snackbar Events ---
    LaunchedEffect(Unit) {
        viewModel.snackbarEvent.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent // Make Scaffold background transparent
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding) // Apply padding from the Scaffold
                .padding(vertical = 8.dp),
        ) {
            // --- Search Bar ---
            OutlinedTextField(
                // ... same as before
                value = searchQuery,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor =Color.White,
                    focusedTrailingIconColor = Color.White,
                    unfocusedTrailingIconColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White
                ),
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Search Videos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            viewModel.clearSearchResults() // Clear results when query is cleared
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    if (searchQuery.isNotBlank()) {
                        viewModel.searchVideos(searchQuery)
                        keyboardController?.hide()
                    }
                }),
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // ... rest of the UI (VideoCategoryRow, etc.) remains the same
                // --- Search Results ---
                if (searchResults.isNotEmpty()) {
                    VideoCategoryRow("Search Results", searchResults) { videoId, title ->
                        navigationProvider.openYoutubePlayer(videoId, title)
                    }
                }

                // --- Categories (only show if not searching) ---
                categoriesWithVideos.forEach { categoryWithVideos ->
                    if (categoryWithVideos.videos.isNotEmpty()) {
                        VideoCategoryRow(
                            title = categoryWithVideos.category.title,
                            videos = categoryWithVideos.videos
                        ) { videoId, title ->
                            navigationProvider.openYoutubePlayer(videoId, title)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCategoryRow(
    title: String,
    videos: List<VideoItem>,
    onVideoClick: (String, String) -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(videos, key = { it.id }) { video ->
                VideoThumbnail(
                    video = video,
                    onClick = { onVideoClick(video.videoId, video.title) })
            }
        }
    }
}

@Composable
fun VideoThumbnail(video: VideoItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            AsyncImage(
                model = video.getThumbnailUrl(),
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp),
                maxLines = 2, // Allow for slightly longer titles
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
