package com.tzh.mamp.ui.screen.quiz

import android.app.Activity
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tzh.mamp.BuildConfig
import com.tzh.mamp.R
import com.tzh.mamp.app.SoundPlayer
import com.tzh.mamp.data.model.Option
import com.tzh.mamp.data.model.QuizQuestionType

@Composable
fun ConsonantQuizScreen(
    viewModel: ConsonantQuizViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val soundPlayer: SoundPlayer = remember { SoundPlayer(context) }
    val phoneticPlayer: SoundPlayer = remember { SoundPlayer(context) }
    val state by viewModel.uiState.collectAsState()

    // Play sound when question changes
    LaunchedEffect(state.question?.id) {
        state.question?.let { question ->
            soundPlayer.release()
            // Try to find consonant file path from repository if needed
            val consonant = viewModel.getConsonantForQuestion(question)
            consonant?.filePath?.let { path ->
                if (state.question?.type == QuizQuestionType.PhoneticToLetter) {
                    val inputStream = context.assets.open(path)
                    soundPlayer.playFromInputStream(inputStream, true)
                }
            }
        }
    }

    if (state.isQuizFinished) {
        LaunchedEffect(Unit) {
            soundPlayer.release()
        }
        QuizCompletionScreen(
            score = state.score, onPlayAgain = { viewModel.restartQuiz() })
    } else {
        state.question?.let { question ->
            QuizContent(
                state = state,
                totalQuestions = viewModel.getTotalQuestionCount(),
                onOptionSelected = {
                    viewModel.setEvent(
                        ConsonantQuizContract.Event.OnOptionSelected(it)
                    )
                },
                onPlaySound = {
                    val consonant = viewModel.getConsonantById(it)
                    consonant?.filePath?.let { path ->
                        val inputStream = context.assets.open(path)
                        phoneticPlayer.playFromInputStream(inputStream)
                    }
                }
            )
        }
    }
}

@Composable
fun QuizContent(
    state: ConsonantQuizContract.State,
    totalQuestions: Int,
    onOptionSelected: (Option) -> Unit,
    onPlaySound: (consonantId: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizHeader(
            currentQuestion = state.question?.id ?: 0,
            totalQuestions = totalQuestions,
            score = state.score
        )

        Spacer(Modifier.height(24.dp))

        QuestionDisplay(text = state.question!!.questionText, state.question.type)

        Spacer(Modifier.height(16.dp))

        OptionsGrid(
            options = state.question.options,
            selectedAnswer = state.selectedAnswer,
            correctAnswerId = state.question.correctAnswerId,
            isAnswered = state.isAnswerCorrect != null,
            onOptionSelected = onOptionSelected,
            questionType = state.question.type,
            onPlaySound = onPlaySound
        )
    }
}

// Header with progress bar and score
@Composable
fun QuizHeader(currentQuestion: Int, totalQuestions: Int, score: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.question, currentQuestion, totalQuestions),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.score, score),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { (currentQuestion.toFloat() / totalQuestions) },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            trackColor = Color(0xFFFFC107), // Amber
            color = Color.White.copy(alpha = 0.3f)
        )
    }
}

// Displays the question in a fun card
@Composable
fun QuestionDisplay(text: String, type: QuizQuestionType) {
    val questionStringId = remember {
        when (type) {
            QuizQuestionType.ConceptToLetter -> R.string.which_letter_starts_the_word_for


            QuizQuestionType.LetterToPhonetic -> R.string.what_sound_does_make
            QuizQuestionType.PhoneticToLetter -> R.string.which_letter_makes_the_sound

            QuizQuestionType.WordToLetter -> R.string.which_letter_starts_the_word
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.7f
            ),
            contentColor = Color.White
        )
    ) {
        Text(
            text = stringResource(questionStringId, text),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun OptionsGrid(
    options: List<Option>,
    selectedAnswer: Option?,
    correctAnswerId: Int,
    isAnswered: Boolean,
    onOptionSelected: (Option) -> Unit,
    onPlaySound: (consonantId: Int) -> Unit,
    questionType: QuizQuestionType
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        options.chunked(2).forEach { rowOptions ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowOptions.forEach { option ->
                    OptionBlob(
                        questionType = questionType,
                        text = option.label,
                        modifier = Modifier.weight(1f),
                        isSelected = selectedAnswer == option,
                        isAnswered = isAnswered,
                        isCorrect = option.id == correctAnswerId,
                        onClick = {
//                            soundPlayer.playFromOption(option, questionType)
                            onOptionSelected(option)
                        },
                        onPlaySound = {
                            onPlaySound(option.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OptionBlob(
    text: String,
    questionType: QuizQuestionType,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    isAnswered: Boolean,
    isCorrect: Boolean,
    onClick: () -> Unit,
    onPlaySound: () -> Unit,
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1f)
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !isAnswered -> Color(0xFF4FC3F7).copy(alpha = 0.5f)
            isSelected && isCorrect -> Color(0xFF8BC34A)
            isSelected && !isCorrect -> Color(0xFFE57373)
            isAnswered && isCorrect -> Color(0xFF8BC34A)
            else -> Color(0xFF4FC3F7).copy(alpha = 0.5f)
        }, animationSpec = tween(500)
    )

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale),
        enabled = !isAnswered,
        onClick = onClick,
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            disabledContainerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            if (questionType == QuizQuestionType.LetterToPhonetic) {
                IconButton(
                    onClick = onPlaySound,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = "Play $text")
                }
            }
        }
    }
}

@Composable
fun QuizCompletionScreen(score: Int, onPlayAgain: () -> Unit) {
    val context = LocalContext.current
    var interstitialAd: InterstitialAd? by remember { mutableStateOf(null) }
    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            BuildConfig.QUIZ_SUCCESS_ADS_KEY,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    Log.d("QuizCompletionScreen", "Ad was loaded.")
                    interstitialAd = ad
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("QuizCompletionScreen", adError.message)
                    interstitialAd = null
                }
            },
        )
    }
    LaunchedEffect(interstitialAd) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("QuizCompletionScreen", "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        interstitialAd = null
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        // Called when fullscreen content failed to show.
                        Log.d("QuizCompletionScreen", "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        interstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        Log.d("QuizCompletionScreen", "Ad showed fullscreen content.")
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d("QuizCompletionScreen", "Ad recorded an impression.")
                    }

                    override fun onAdClicked() {
                        // Called when ad is clicked.
                        Log.d("QuizCompletionScreen", "Ad was clicked.")
                    }
                }
            interstitialAd?.show(context as Activity)
        }
    }


    if (interstitialAd == null) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TrophyAnimation(modifier = Modifier.size(150.dp))
            Spacer(Modifier.height(24.dp))
            Text("Great Job!", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text("Your Score: $score", fontSize = 32.sp, color = Color.White.copy(alpha = 0.8f))
            Spacer(Modifier.height(32.dp))
            Button(
                onClick = onPlayAgain,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                modifier = Modifier.height(50.dp)
            ) {
                Text("Play Again", fontSize = 20.sp, color = Color.White)
            }
        }
    }

}


@Composable
fun TrophyAnimation(
    lottieRes: Int = R.raw.reward, modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    LottieAnimation(
        composition = composition, progress = { progress }, modifier = modifier
    )
}
