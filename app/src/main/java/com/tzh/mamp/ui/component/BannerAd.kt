package com.tzh.mamp.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String,
) {
    val context = LocalContext.current

    val adView = remember {
        AdView(context).apply {
            // Set the adaptive banner ad size with a given width.
            val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, 360)
            setAdSize(adSize)
            Log.e("jhgjkgihuib", adUnitId)
            this.adUnitId = adUnitId
            loadAd(AdRequest.Builder().build())
        }
    }
    ADSBanner(
        adView, modifier
    )
}

@Composable
private fun ADSBanner(
    adView: AdView,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    // Ad load does not work in preview mode because it requires a network connection.
    if (LocalInspectionMode.current) {
        Box { Text(text = "Google Mobile Ads preview banner.", modifier.align(Alignment.Center)) }
        return
    }

    AndroidView(modifier = modifier.wrapContentSize(), factory = { adView })

    // Pause and resume the AdView when the lifecycle is paused and resumed.
    LifecycleResumeEffect(adView) {
        adView.resume()
        onPauseOrDispose { adView.pause() }
    }

//    AndroidViewBinding(
//        factory = { inflater, parent, _ ->
//            val binding = AdBannerBinding.inflate(inflater, parent, false)
//            val adView = AdView(parent.context).apply {
//                setAdSize(AdSize.BANNER)
//                Log.e("jhgjkgihuib",BuildConfig.ADS_KEY)
//                adUnitId = BuildConfig.ADS_KEY
//                loadAd(AdRequest.Builder().build())
//            }
//            binding.adContainer.addView(adView)
//            binding
//        },
//        modifier = modifier.pointerInput(Unit) {
//            // Consume all drag/pointer events to prevent Compose crash
//            awaitPointerEventScope {
//                while (true) {
//                    awaitPointerEvent()
//                }
//            }
//        }
//    )
//
////    AndroidView(
////        modifier = modifier,
////        factory = { context ->
////            FrameLayout(context).apply {
////                val adView = AdView(context).apply {
////                    setAdSize(AdSize.BANNER)
////                    adUnitId = BuildConfig.ADS_KEY
////                    loadAd(AdRequest.Builder().build())
////                    setOnDragListener { _, _ -> true }
////                }
////                addView(adView)
////                // Prevent drag & drop interactions from bubbling into Compose
////
////            }
////        }
////    )
}
