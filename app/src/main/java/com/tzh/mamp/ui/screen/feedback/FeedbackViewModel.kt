package com.tzh.mamp.ui.screen.feedback

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _feedback = MutableStateFlow("")
    val feedback: StateFlow<String> = _feedback

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    fun updateFeedback(text: String) {
        _feedback.value = text
    }

    fun sendFeedback(onSuccess: () -> Unit, onError: () -> Unit) {
        val text = feedback.value.trim()
        if (text.isEmpty()) return

        _isSending.value = true

        val feedbackData = mapOf(
            "message" to text,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("feedback")
            .add(feedbackData)
            .addOnSuccessListener {
                _isSending.value = false
                _feedback.value = ""
                onSuccess()
            }
            .addOnFailureListener {
                _isSending.value = false
                onError()
            }
    }
}
