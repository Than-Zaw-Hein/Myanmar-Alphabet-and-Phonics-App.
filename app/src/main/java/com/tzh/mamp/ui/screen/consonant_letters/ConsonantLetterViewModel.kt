package com.tzh.mamp.ui.screen.consonant_letters

import androidx.lifecycle.ViewModel
import com.tzh.mamp.data.repository.MyanmarLetterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsonantLetterViewModel @Inject constructor(
    repository: MyanmarLetterRepository,
) : ViewModel() {
    val alphabets = repository.consonants

}