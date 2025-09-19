package com.tzh.mamp.provider

import com.tzh.mamp.app.AlphabetType

interface NavigationProvider {

    fun openSetting()
    fun openVideoPlyaer()

    fun openToAlphabet(index: Int, type: AlphabetType)
    fun openToVowel(index: Int)

    fun onBack()

}