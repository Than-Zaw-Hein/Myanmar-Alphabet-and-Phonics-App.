package com.tzh.mamp.data.repository

import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.Option
import com.tzh.mamp.data.model.QuizQuestion
import com.tzh.mamp.data.model.QuizQuestionType

object QuizQuestionFactory {

    fun generate(consonants: List<Consonant>): List<QuizQuestion> {
        val commonConsonants = consonants
//            .filterNot {
//            it.example.contains("rare", ignoreCase = true)
//        }

        val allQuestions = commonConsonants.map { consonant ->
            val generators = listOf(
                { createPhoneticToLetter(consonant, commonConsonants) },
                { createLetterToPhonetic(consonant, commonConsonants) },
                { createWordToLetter(consonant, commonConsonants) }
            ).toMutableList()

            if (consonant.example.contains("(")) {
                generators.add { createConceptToLetter(consonant, commonConsonants) }
            }

            generators.random().invoke()
        }

        return allQuestions.shuffled().mapIndexed { index, q -> q.copy(id = index + 1) }
    }

    private fun wrongOptions(
        all: List<Consonant>,
        answer: Consonant,
        selector: (Consonant) -> Option
    ) = all.filter { it.id != answer.id }.shuffled().take(3).map(selector)

    private fun createPhoneticToLetter(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.phonetic,// context.getString(R.string.which_letter_makes_the_sound, answer.phonetic),
        options = (wrongOptions(all, answer) {
            Option(it.id, it.letter)
        } + Option(answer.id, answer.letter)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.PhoneticToLetter
    )

    private fun createLetterToPhonetic(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.letter,// context.getString(R.string.what_sound_does_make, answer.letter),
        options = (wrongOptions(all, answer) { Option(it.id, it.phonetic) } + Option(
            answer.id,
            answer.phonetic
        )).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.LetterToPhonetic
    )

    private fun createWordToLetter(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.example,// context.getString(R.string.which_letter_starts_the_word, ),
        options =(wrongOptions(all, answer) {
            Option(it.id, it.letter)
        } + Option(answer.id, answer.letter)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.WordToLetter
    )

    private fun createConceptToLetter(answer: Consonant, all: List<Consonant>): QuizQuestion {
        val concept = answer.example

        return QuizQuestion(
            id = 0,
            questionText = concept,//context.getString(R.string.which_letter_starts_the_word_for, ),
            options = (wrongOptions(all, answer) {
                Option(it.id, it.letter)
            } + Option(answer.id, answer.letter)).shuffled(),
            correctAnswerId = answer.id,
            type = QuizQuestionType.ConceptToLetter
        )
    }
}
