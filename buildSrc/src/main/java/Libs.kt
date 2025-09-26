object Libs {

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val kotlinLib = "org.jetbrains.kotlin:kotlin-stdlib:1.9.0"
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val material = "com.google.android.material:material:${Versions.material}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    }

    object Coroutines {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val android =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    }

    object Compose {
        const val timber = "com.jakewharton.timber:timber:5.0.1"
        const val splashscreen = "androidx.core:core-splashscreen:1.0.1"
        const val bom = "androidx.compose:compose-bom:${Versions.composeBom}"
        const val ui = "androidx.compose.ui:ui"
        const val viewbinding = "androidx.compose.ui:ui-viewbinding:1.9.0"
        const val material3 = "androidx.compose.material3:material3:1.4.0-alpha02"
        const val preview = "androidx.compose.ui:ui-tooling-preview"
        const val tooling = "androidx.compose.ui:ui-tooling"
        const val runtime = "androidx.compose.runtime:runtime"
        const val foundation = "androidx.compose.foundation:foundation"
        const val icons = "androidx.compose.material:material-icons-core"
        const val iconsExtended = "androidx.compose.material:material-icons-extended"
        const val activity = "androidx.activity:activity-compose:1.9.3"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
        const val constraintLayout =
            "androidx.constraintlayout:constraintlayout-compose:${Versions.constraintLayout}"
        const val lottie = "com.airbnb.android:lottie-compose:${Versions.lottie}"
        const val paging = "androidx.paging:paging-compose:${Versions.pagingCompose}"
        const val adaptive = "androidx.compose.material3.adaptive:adaptive:1.1.0"

        const val coil = "io.coil-kt:coil-compose:${Versions.coil}"

        const val coil_gif = "io.coil-kt:coil-gif:2.1.0"
        const val googleFonts = "androidx.compose.ui:ui-text-google-fonts:${Versions.googleFonts}"
        const val manifest = "androidx.compose.ui:ui-test-manifest"

        const val palette = "androidx.palette:palette:1.0.0"
    }

    object Navigation {
        const val navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
        const val destinationsCore =
            "io.github.raamcosta.compose-destinations:core:${Versions.destinations}"
        const val destinationsKsp =
            "io.github.raamcosta.compose-destinations:ksp:${Versions.destinations}"
        const val destinationsAnimation =
            "io.github.raamcosta.compose-destinations:animations-core:${Versions.destinations}"
    }

    object Accompanist {
        const val swipeRefresh =
            "com.google.accompanist:accompanist-swiperefresh:${Versions.accompanist}"
        const val systemUi =
            "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"
        const val insets = "com.google.accompanist:accompanist-insets:${Versions.accompanist}"
        const val placeholder =
            "com.google.accompanist:accompanist-placeholder-material:${Versions.accompanist}"
        const val navigationMaterial =
            "com.google.accompanist:accompanist-navigation-material:${Versions.accompanist}"
        const val permissions =
            "com.google.accompanist:accompanist-permissions:${Versions.accompanist}"
        const val pager = "com.google.accompanist:accompanist-pager:${Versions.accompanist}"
        const val paging = "androidx.paging:paging-compose:1.0.0-alpha17"
        const val indicators =
            "com.google.accompanist:accompanist-pager-indicators:${Versions.accompanist}"
        const val webview = "com.google.accompanist:accompanist-webview:0.24.6-alpha"
    }

    object Network {
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
        const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

        const val moshi = "com.squareup.moshi:moshi-kotlin:1.13.0"
        const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:1.13.0"
        const val moshiLazyAdapter = "com.serjltt.moshi:moshi-lazy-adapters:2.2"

        const val kotlinxSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
        const val kotlinxSerializationRetrofit =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val gson = "com.google.code.gson:gson:2.11.0"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

        const val chuckerDebug = "com.github.chuckerteam.chucker:library:3.5.2"
        const val chuckerRelease = "com.github.chuckerteam.chucker:library-no-op:3.5.2"

        // Ktor
        const val ktorCore = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
        const val ktorSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
        const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
        const val ktorNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"

        const val ktorCio = "io.ktor:ktor-client-cio:2.3.5"
        const val webScoket = "io.ktor:ktor-client-websockets:2.3.5"
    }

    object Storage {
        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val datastorePref = "androidx.datastore:datastore-preferences:${Versions.datastore}"
        const val datastoreProto = "androidx.datastore:datastore:${Versions.datastore}"
        const val security = "androidx.security:security-crypto:${Versions.security}"
    }

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"

        // Only use KTX versions
        const val playServicesAds =
            "com.google.android.gms:play-services-ads:${Versions.playServicesAds}"

        const val adsMobileSdk =
            "com.google.android.libraries.ads.mobile.sdk:ads-mobile-sdk:${Versions.adsMobileSdk}"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val messaging = "com.google.firebase:firebase-messaging-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val config = "com.google.firebase:firebase-config-ktx"
        const val database = "com.google.firebase:firebase-database-ktx"
    }

    object Hilt {
        const val android = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val compiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
        const val work = "androidx.hilt:hilt-work:${Versions.hiltWork}"
        const val compose = "androidx.hilt:hilt-navigation-compose:1.2.0"
        const val workRuntime = "androidx.work:work-runtime-ktx:${Versions.workManager}"
    }

    object Media3 {
        const val exoplayer = "androidx.media3:media3-exoplayer:${Versions.media3}"
        const val ui = "androidx.media3:media3-ui:${Versions.media3}"
        const val exoplayerHls = "androidx.media3:media3-exoplayer-hls:${Versions.media3}"
        const val exoplayerDash = "androidx.media3:media3-exoplayer-dash:${Versions.media3}"
        const val uiCompose = "androidx.media3:media3-ui-compose:${Versions.media3}"
        const val datasource = "androidx.media3:media3-datasource:${Versions.media3}"
        const val datasourceOkhttp = "androidx.media3:media3-datasource-okhttp:${Versions.media3}"
        const val database = "androidx.media3:media3-database:${Versions.media3}"
        const val session = "androidx.media3:media3-session:${Versions.media3}"
        const val workManager = "androidx.media3:media3-exoplayer-workmanager:1.8.0"
        const val cast = "androidx.media3:media3-cast:${Versions.media3}"
    }

    object GoogleADS {
        const val ads = "com.google.android.gms:play-services-ads:24.5.0"
    }

    object YouTubePlayer {
        const val core = "com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.2"
        const val custom = "com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.2"
    }

    object Testing {
        const val junit = "junit:junit:${Versions.junit}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val composeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.composeJunit}"
        const val Coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.1"
        const val Truth = "com.google.truth:truth:1.1.3"
        const val Robolectric = "org.robolectric:robolectric:4.7.3"
        const val Turbine = "app.cash.turbine:turbine:0.7.0"
        const val Mockk = "io.mockk:mockk:1.12.3"
        const val Okhttp = "com.squareup.okhttp3:mockwebserver:5.0.0-alpha.6"
    }
}
