package com.tzh.mamp.ui.screen.vowels

import androidx.lifecycle.ViewModel
import com.tzh.mamp.data.repository.MyanmarLetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class VowelLetterViewModel @Inject constructor(
    repository: MyanmarLetterRepository,
) : ViewModel() {
    val vowels = repository.vowelLetters

}