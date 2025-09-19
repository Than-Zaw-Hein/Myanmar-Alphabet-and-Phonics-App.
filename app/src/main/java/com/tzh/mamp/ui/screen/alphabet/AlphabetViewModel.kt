package com.tzh.mamp.ui.screen.alphabet

import androidx.lifecycle.ViewModel
import com.tzh.mamp.data.repository.MyanmarLetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlphabetViewModel @Inject constructor(
    private val repository: MyanmarLetterRepository,
) : ViewModel() {
    val consonent = repository.consonants
    val vowelLetter = repository.vowelLetters
}