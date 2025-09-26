package com.tzh.mamp.app

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.annotation.RawRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class SoundPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    var isPlaying by mutableStateOf(false)


    fun playSound(@RawRes soundResId: Int,isLoop: Boolean = false) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, soundResId).apply {
            isLooping = isLoop
        }
        mediaPlayer?.start()
    }

    fun playFromInputStream(
        inputStream: InputStream,
        isLoop: Boolean = false
    ) {
        mediaPlayer?.release()
        isPlaying = true
        try {
            // Create a temp file
            val tempFile = File.createTempFile("temp_sound", ".mp3", context.cacheDir)
            tempFile.deleteOnExit()

            // Write InputStream to temp file
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            outputStream.close()

            // Play the temp file
            mediaPlayer = MediaPlayer().apply {
                setDataSource(tempFile.absolutePath)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                isLooping = isLoop
                prepare()
                start()
            }

            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
            }
        } catch (e: IOException) {
            isPlaying = false
            e.printStackTrace()
        }
    }

    fun release() {
        isPlaying = false
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
