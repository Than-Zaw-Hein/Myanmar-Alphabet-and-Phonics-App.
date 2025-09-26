package com.tzh.mamp.ui.screen.quiz

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tzh.mamp.data.model.Consonant
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
    @ApplicationContext private val context: Context,
    private val repository: MyanmarLetterRepository,
    private val languageProvider: LanguageProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConsonantQuizContract.State())
    val uiState: StateFlow<ConsonantQuizContract.State> = _uiState

    private var questions: List<QuizQuestion> = emptyList()
    private var currentQuestionIndex = 0

    init {
        loadQuestions()
    }

    fun getTotalQuestionCount(): Int = questions.size

    fun getConsonantForQuestion(question: QuizQuestion): Consonant? {
        return when (question.type) {
            QuizQuestionType.PhoneticToLetter,
            QuizQuestionType.LetterToPhonetic,
            QuizQuestionType.WordToLetter,
            QuizQuestionType.ConceptToLetter -> {
                repository.consonants.find { it.id == question.correctAnswerId }
            }
        }
    }
    fun getConsonantById(id: Int): Consonant? {
        return  repository.consonants.find { it.id ==id }
    }
    fun setEvent(event: ConsonantQuizContract.Event) {
        when (event) {
            is ConsonantQuizContract.Event.OnOptionSelected -> {
                if (_uiState.value.isAnswerCorrect != null) return // Prevent double clicks

                val current = _uiState.value.question ?: return

                viewModelScope.launch {
                    val isCorrect = event.option.id == current.correctAnswerId

                    _uiState.update {
                        it.copy(
                            selectedAnswer = event.option,
                            isAnswerCorrect = isCorrect,
                            score = if (isCorrect) it.score + 1 else it.score
                        )
                    }
                    delay(2000) // show result before moving on
                    loadNextQuestion()
                }
            }

            ConsonantQuizContract.Event.OnNextClicked -> Unit // no-op
        }
    }

    fun restartQuiz() {
        viewModelScope.launch {
            // Reset indexes and state
            currentQuestionIndex = 0
            _uiState.value = ConsonantQuizContract.State(
                question = null,
                selectedAnswer = null,
                isAnswerCorrect = null,
                score = 0,
                isQuizFinished = false,
            )

            // Reload questions
            loadQuestions()
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            val consonants = repository.consonants
            questions = QuizQuestionFactory.generate(consonants)
            _uiState.update { it.copy(question = questions.firstOrNull()) }
        }
    }

    private fun loadNextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questions.size) {
            _uiState.update {
                ConsonantQuizContract.State(
                    question = questions[currentQuestionIndex],
                    score = it.score
                )
            }
        } else {
            _uiState.update { it.copy(isQuizFinished = true) }
        }
    }
}
