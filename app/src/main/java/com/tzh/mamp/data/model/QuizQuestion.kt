package com.tzh.mamp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuizQuestion(
    @SerialName("id")
    val id: Int,

    @SerialName("question_text")
    val questionText: String,

    @SerialName("options")
    val options: List<Option>,

    @SerialName("correct_answer")
    val correctAnswerId: Int,

    @SerialName("type")
    val type: QuizQuestionType
)

@Serializable
data class Option(
    @SerialName("id")
    val id: Int,
    @SerialName("label")
    val label: String
)

@Serializable
sealed class QuizQuestionType {
    @Serializable
    @SerialName("phonetic_to_letter")
    object PhoneticToLetter : QuizQuestionType()

    @Serializable
    @SerialName("letter_to_phonetic")
    object LetterToPhonetic : QuizQuestionType()

    @Serializable
    @SerialName("word_to_letter")
    object WordToLetter : QuizQuestionType()

    @Serializable
    @SerialName("concept_to_letter")
    object ConceptToLetter : QuizQuestionType()
}