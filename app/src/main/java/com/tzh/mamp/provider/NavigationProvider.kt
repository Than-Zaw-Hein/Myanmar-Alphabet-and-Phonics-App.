package com.tzh.mamp.provider

import com.tzh.mamp.app.AlphabetType

interface NavigationProvider {

    fun openSetting()
    fun openVideoPlayer()
    fun openToAlphabet(index: Int, type: AlphabetType)
    fun onBack()

    fun openTracing(consonant: String)

    fun openYoutubePlayer(videoId: String,title:String)

}