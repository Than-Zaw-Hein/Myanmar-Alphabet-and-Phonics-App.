package com.tzh.mamp.ui.screen.quiz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.mamp.app.util.QuizMode
import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.Option
import com.tzh.mamp.data.model.QuizQuestion
import com.tzh.mamp.data.model.QuizQuestionType
import com.tzh.mamp.data.repository.MyanmarLetterRepository
import com.tzh.mamp.data.repository.QuizQuestionFactory
import com.tzh.mamp.provider.LanguageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ConsonantQuizViewModel @Inject constructor(
    private val repository: MyanmarLetterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsonantQuizContract.State())
    val uiState: StateFlow<ConsonantQuizContract.State> = _uiState

    private var questions: List<QuizQuestion> = emptyList()
    private var currentQuestionIndex = 0

    fun loadQuestions(mode: QuizMode = QuizMode.Standard) {
        viewModelScope.launch {
            val consonants = repository.consonants
            questions = when (mode) {
                QuizMode.Standard -> QuizQuestionFactory.generate(consonants)
                QuizMode.Daily -> QuizQuestionFactory.generateDaily(consonants)
                QuizMode.MiniGame -> QuizQuestionFactory.generateMiniGame(consonants)
            }
            currentQuestionIndex = 0
            _uiState.update {
                it.copy(
                    question = questions.firstOrNull(),
                    selectedAnswer = null,
                    isAnswerCorrect = null,
                    score = 0,
                    isQuizFinished = false,
                    progress = QuizProgress(questions = questions, currentIndex = 0)
                )
            }
        }
    }

    fun setEvent(event: ConsonantQuizContract.Event) {
        when (event) {
            is ConsonantQuizContract.Event.OnOptionSelected -> handleOptionSelected(event.option)
            ConsonantQuizContract.Event.OnNextClicked -> loadNextQuestion()
        }
    }

    private fun handleOptionSelected(option: Option) {
        val current = _uiState.value.question ?: return
        if (_uiState.value.isAnswerCorrect != null) return // Prevent double tap

        viewModelScope.launch {
            val isCorrect = option.id == current.correctAnswerId

            _uiState.update {
                it.copy(
                    selectedAnswer = option,
                    isAnswerCorrect = isCorrect,
                    score = if (isCorrect) it.score + 1 else it.score
                )
            }
            delay(2000)
            loadNextQuestion()
        }
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++
        val progress = QuizProgress(questions, currentQuestionIndex)
        if (progress.isFinished) {
            _uiState.update { it.copy(isQuizFinished = true, progress = progress) }
        } else {
            _uiState.update {
                it.copy(
                    question = progress.currentQuestion,
                    selectedAnswer = null,
                    isAnswerCorrect = null,
                    progress = progress
                )
            }
        }
    }

    fun getConsonantForQuestion(question: QuizQuestion) =
        repository.consonants.find { it.id == question.correctAnswerId }

    fun getConsonantById(id: Int) =
        repository.consonants.find { it.id == id }
}
