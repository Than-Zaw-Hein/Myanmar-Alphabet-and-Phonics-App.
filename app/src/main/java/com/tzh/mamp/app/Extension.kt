package com.tzh.mamp.app

import android.app.Activity
import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import com.tzh.mamp.data.model.QuizQuestionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.min

object Extension {
    fun Activity.setScreenOrientation(orientation: Int) {
        requestedOrientation = orientation
    }
}
fun SoundPlayer.playFromOption(option: String, type: QuizQuestionType) {
    // For now, just replay the sound if option is a consonant
    // You can customize per questionType if needed
    // Example: map option string -> filePath
    if (type == QuizQuestionType.PhoneticToLetter){

    }
}


fun createConsonantBitmap(
    width: Int, height: Int, consonant: String
): Bitmap {
    // Create a mutable bitmap
    val bitmap = createBitmap(width, height)

    // Create a native Canvas to draw on the bitmap
    val canvas = android.graphics.Canvas(bitmap)

    // Prepare the Paint
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLUE
        textAlign = android.graphics.Paint.Align.CENTER
        textSize = min(width, height) / 1.2f
        isAntiAlias = true
        setShadowLayer(8f, 0f, 0f, android.graphics.Color.CYAN)
    }
    val yCenter = height / 2f - (paint.descent() + paint.ascent()) / 2
    canvas.drawText(consonant, width / 2f, yCenter, paint)

//    // Draw the text centered
//    canvas.drawText(
//        consonant, width / 2f, height / 2f, // vertical centering
//        paint
//    )
    return bitmap
}

suspend fun isTracingCorrect(
    userBitmap: Bitmap,
    consonantBitmap: Bitmap,
    tolerance: Float = 0.8f, // % of coverage needed
    radius: Int = 4          // how forgiving tracing can be (px around letter)
): Boolean = withContext(Dispatchers.Default) {
    // Step 1: Scale down to improve performance
    val targetSize = 200
    val scaledConsonant = consonantBitmap.scale(targetSize, targetSize)
    val scaledUser = userBitmap.scale(targetSize, targetSize)

    val width = scaledConsonant.width
    val height = scaledConsonant.height

    // Step 2: Get pixels into arrays (fast access)
    val consonantPixels = IntArray(width * height)
    scaledConsonant.getPixels(consonantPixels, 0, width, 0, 0, width, height)

    val userPixels = IntArray(width * height)
    scaledUser.getPixels(userPixels, 0, width, 0, 0, width, height)

    // Step 3: Iterate only once
    var totalLetterPixels = 0
    var matchedPixels = 0
    var strayPixels = 0

    for (y in 0 until height) {
        for (x in 0 until width) {
            val index = y * width + x
            val isLetterPixel = android.graphics.Color.alpha(consonantPixels[index]) > 50
            val isUserPixel = android.graphics.Color.alpha(userPixels[index]) > 50

            if (isLetterPixel) {
                totalLetterPixels++

                if (isUserPixel) {
                    matchedPixels++
                } else {
                    // Check nearby pixels within radius (but much smaller loop than before)
                    var foundNearby = false
                    for (dy in -radius..radius) {
                        for (dx in -radius..radius) {
                            val nx = x + dx
                            val ny = y + dy
                            if (nx in 0 until width && ny in 0 until height) {
                                val nIndex = ny * width + nx
                                if (android.graphics.Color.alpha(userPixels[nIndex]) > 50) {
                                    matchedPixels++
                                    foundNearby = true
                                    break
                                }
                            }
                        }
                        if (foundNearby) break
                    }
                }
            } else if (isUserPixel) {
                strayPixels++
            }
        }
    }

    if (totalLetterPixels == 0) return@withContext false

    // Step 4: Calculate scores
    val coverage = matchedPixels.toFloat() / totalLetterPixels
    val strayPenalty = strayPixels.toFloat() / (width * height)

    return@withContext coverage - strayPenalty >= tolerance
}

