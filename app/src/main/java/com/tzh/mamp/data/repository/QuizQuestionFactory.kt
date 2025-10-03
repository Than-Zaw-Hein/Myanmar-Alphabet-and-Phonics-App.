package com.tzh.mamp.data.repository

import com.tzh.mamp.data.model.Consonant
import com.tzh.mamp.data.model.Option
import com.tzh.mamp.data.model.QuizQuestion
import com.tzh.mamp.data.model.QuizQuestionType
object QuizQuestionFactory {

    fun generate(consonants: List<Consonant>): List<QuizQuestion> {
        return generateQuestions(consonants).shuffled().mapIndexed { index, q -> q.copy(id = index + 1) }
    }

    fun generateDaily(consonants: List<Consonant>): List<QuizQuestion> {
        if (consonants.isEmpty()) return emptyList()

        // Deterministic daily selection
        val todaySeed = java.time.LocalDate.now().toEpochDay().toInt()
        val random = kotlin.random.Random(todaySeed)

        val count = (3..5).random(random)
        val selected = consonants.shuffled(random).take(count)

        return generateQuestions(selected, random).shuffled(random).mapIndexed { index, q ->
            q.copy(id = index + 1)
        }
    }

    fun generateMiniGame(consonants: List<Consonant>): List<QuizQuestion> {
        // Example mini-game logic: pick 10 fastest questions
        val random = kotlin.random.Random(System.currentTimeMillis())
        val selected = consonants.shuffled(random).take(10)
        return generateQuestions(selected, random).shuffled(random).mapIndexed { index, q ->
            q.copy(id = index + 1)
        }
    }

    // Internal helper
    private fun generateQuestions(
        consonants: List<Consonant>,
        random: kotlin.random.Random = kotlin.random.Random.Default
    ): List<QuizQuestion> {
        return consonants.map { consonant ->
            val generators = mutableListOf(
                { createPhoneticToLetter(consonant, consonants) },
                { createLetterToPhonetic(consonant, consonants) },
                { createWordToLetter(consonant, consonants) }
            )

            if (consonant.example.contains("(")) {
                generators.add { createConceptToLetter(consonant, consonants) }
            }

            generators.random(random).invoke()
        }
    }

    private fun wrongOptions(
        all: List<Consonant>,
        answer: Consonant,
        selector: (Consonant) -> Option
    ) = all.filter { it.id != answer.id }.shuffled().take(3).map(selector)

    private fun createPhoneticToLetter(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.phonetic,
        options = (wrongOptions(all, answer) { Option(it.id, it.letter) } + Option(answer.id, answer.letter)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.PhoneticToLetter
    )

    private fun createLetterToPhonetic(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.letter,
        options = (wrongOptions(all, answer) { Option(it.id, it.phonetic) } + Option(answer.id, answer.phonetic)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.LetterToPhonetic
    )

    private fun createWordToLetter(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.example,
        options = (wrongOptions(all, answer) { Option(it.id, it.letter) } + Option(answer.id, answer.letter)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.WordToLetter
    )

    private fun createConceptToLetter(answer: Consonant, all: List<Consonant>) = QuizQuestion(
        id = 0,
        questionText = answer.example,
        options = (wrongOptions(all, answer) { Option(it.id, it.letter) } + Option(answer.id, answer.letter)).shuffled(),
        correctAnswerId = answer.id,
        type = QuizQuestionType.ConceptToLetter
    )
}

