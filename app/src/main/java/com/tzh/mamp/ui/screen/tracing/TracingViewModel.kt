package com.tzh.mamp.ui.screen.tracing

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.mamp.app.isTracingCorrect
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TracingViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(TracingUiState())
    val uiState = _uiState.asStateFlow()

    private var checkingJob: Job? = null

    fun onEvent(event: TracingUiEvent) {
        when (event) {
            is TracingUiEvent.HideFail -> {
                _uiState.value = _uiState.value.copy(showFail = null)
            }

            is TracingUiEvent.HideIntro -> {
                _uiState.value = _uiState.value.copy(showIntro = false)
            }

            is TracingUiEvent.Reset -> {
                _uiState.value = TracingUiState()
            }

            is TracingUiEvent.CheckLatestDrawing -> {
                viewModelScope.launch {
                    checkingJob?.cancelAndJoin()
                    checkingJob = launch(Dispatchers.IO) {
                        Timber.tag("START JOB").e("TRUE")
                        _uiState.value = _uiState.value.copy(isChecking = true)
                        delay(1500)
                        val userBitmap = event.getUserBitmap() ?: return@launch
                        val correct =
                            isTracingCorrect(userBitmap, event.consonantBitmap, tolerance = 0.65f)
                        _uiState.value = _uiState.value.copy(
                            showReward = correct, isChecking = false, showFail = !correct
                        )
                        Timber.tag("END JOB").e("TRUE")
                    }
                }
            }
        }
    }
}

data class TracingUiState(
    val showIntro: Boolean = true,
    val showReward: Boolean = false,
    val showFail: Boolean? = false,
    val isChecking: Boolean = false,
)

sealed class TracingUiEvent {
    object HideIntro : TracingUiEvent()
    object Reset : TracingUiEvent()
    data class CheckLatestDrawing(
        val getUserBitmap: () -> Bitmap?, val consonantBitmap: Bitmap
    ) : TracingUiEvent()

    data object HideFail : TracingUiEvent()
}

