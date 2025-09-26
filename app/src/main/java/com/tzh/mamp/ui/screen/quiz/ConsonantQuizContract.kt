package com.tzh.mamp.ui.screen.quiz

import com.tzh.mamp.data.model.Option
import com.tzh.mamp.data.model.QuizQuestion


class ConsonantQuizContract {

    sealed class Event {
        data class OnOptionSelected(val option: Option) : Event()
        object OnNextClicked : Event()
    }

    data class State(
        val question: QuizQuestion? = null,
        val selectedAnswer: Option? = null,
        val isAnswerCorrect: Boolean? = null,
        val score: Int = 0,
        val isQuizFinished: Boolean = false
    )
}