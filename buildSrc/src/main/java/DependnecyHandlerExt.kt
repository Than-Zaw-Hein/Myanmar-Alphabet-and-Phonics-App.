import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

// -------------------------------
// Basic dependency helpers
// -------------------------------
fun DependencyHandler.implementation(dep: String) = add("implementation", dep)
fun DependencyHandler.implementation(dep: Any) = add("implementation", dep)
fun DependencyHandler.testImplementation(dep: String) = add("testImplementation", dep)
fun DependencyHandler.androidTestImplementation(dep: String) = add("androidTestImplementation", dep)
fun DependencyHandler.debugImplementation(dep: String) = add("debugImplementation", dep)
fun DependencyHandler.kapt(dep: String) = add("kapt", dep)
fun DependencyHandler.ksp(dep: String) = add("ksp", dep)
fun DependencyHandler.addPlatform(dep: String) = add("implementation", platform(dep))

fun DependencyHandler.moduleImplementation(module: String) = add("implementation", project(module))
fun DependencyHandler.useModule(moduleName: String) = moduleImplementation(moduleName)

// -------------------------------
// AndroidX / Core
// -------------------------------
fun DependencyHandler.supportLib() {

    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.kotlinLib)
    implementation(Libs.AndroidX.appCompat)
    implementation(Libs.AndroidX.material)
    implementation(Libs.AndroidX.lifecycle)
    implementation(Libs.Coroutines.core)
    implementation(Libs.Coroutines.android)
    implementation(Libs.Compose.timber)

}

// -------------------------------
// Compose
// -------------------------------
fun DependencyHandler.compose() {

    implementation(platform(Libs.Compose.bom))
    implementation(Libs.Compose.ui)
    implementation(Libs.Compose.viewbinding)
    implementation(Libs.Compose.material3)
    implementation(Libs.Compose.splashscreen)
    implementation(Libs.Compose.preview)
    implementation(Libs.Compose.runtime)
    implementation(Libs.Compose.foundation)
    implementation(Libs.Compose.icons)
    implementation(Libs.Compose.iconsExtended)
    implementation(Libs.Compose.activity)
    implementation(Libs.Compose.viewModel)
    implementation(Libs.Compose.constraintLayout)
    implementation(Libs.Compose.lottie)
    implementation(Libs.Compose.palette)
    implementation(Libs.Compose.paging)
    implementation(Libs.Compose.adaptive)
    implementation(Libs.Compose.coil)
    implementation(Libs.Compose.coil_gif)
    implementation(Libs.Compose.googleFonts)

    androidTestImplementation(Libs.Testing.composeJunit)
    debugImplementation(Libs.Compose.tooling)
    debugImplementation(Libs.Compose.manifest)
}

// -------------------------------
// Navigation
// -------------------------------
fun DependencyHandler.navigation() {
    implementation(Libs.Navigation.navigation)
    implementation(Libs.Navigation.destinationsCore)
    ksp(Libs.Navigation.destinationsKsp)
    implementation(Libs.Navigation.destinationsAnimation)
}

// -------------------------------
// Accompanist
// -------------------------------
fun DependencyHandler.accompanist() {
    implementation(Libs.Accompanist.swipeRefresh)
    implementation(Libs.Accompanist.systemUi)
    implementation(Libs.Accompanist.insets)
    implementation(Libs.Accompanist.placeholder)
    implementation(Libs.Accompanist.navigationMaterial)
    implementation(Libs.Accompanist.permissions)
    implementation(Libs.Accompanist.pager)
    implementation(Libs.Accompanist.paging)
    implementation(Libs.Accompanist.indicators)
    implementation(Libs.Accompanist.webview)
}

// -------------------------------
// Network
// -------------------------------
fun DependencyHandler.network() {
    implementation(Libs.Network.retrofit)
    implementation(Libs.Network.retrofitMoshi)
    implementation(Libs.Network.okhttp)
    implementation(Libs.Network.loggingInterceptor)
    implementation(Libs.Network.moshi)
    ksp(Libs.Network.moshiCodegen)
    implementation(Libs.Network.moshiLazyAdapter)
    implementation(Libs.Network.kotlinxSerialization)
    implementation(Libs.Network.kotlinxSerializationRetrofit)

    implementation(Libs.Network.gson)
    implementation(Libs.Network.gsonConverter)

    // Ktor
    implementation(Libs.Network.ktorCore)
    implementation(Libs.Network.ktorAndroid)
    implementation(Libs.Network.ktorSerialization)
    implementation(Libs.Network.ktorLogging)
    implementation(Libs.Network.ktorNegotiation)
    implementation(Libs.Network.ktorCio)
    implementation(Libs.Network.webScoket)
}

// -------------------------------
// Storage
// -------------------------------
fun DependencyHandler.storage() {
    implementation(Libs.Storage.roomKtx)
    ksp(Libs.Storage.roomCompiler)
    implementation(Libs.Storage.datastorePref)
    implementation(Libs.Storage.datastoreProto)
    implementation(Libs.Storage.security)
}

// -------------------------------
// Firebase
// -------------------------------
fun DependencyHandler.firebase() {
    addPlatform(Libs.Firebase.bom)
    implementation(Libs.Firebase.analytics)
    implementation(Libs.Firebase.crashlytics)
    implementation(Libs.Firebase.messaging)
    implementation(Libs.Firebase.firestore)
    implementation(Libs.Firebase.config)
    implementation(Libs.Firebase.database)
    implementation(Libs.Firebase.playServicesAds)
//    implementation(Libs.Firebase.adsMobileSdk)

}
// -------------------------------
// Hilt
// -------------------------------
fun DependencyHandler.hilt() {
    implementation(Libs.Hilt.android)
    implementation(Libs.Hilt.work)
    implementation(Libs.Hilt.workRuntime)
    ksp(Libs.Hilt.compiler)
    implementation(Libs.Hilt.compose)
}

// -------------------------------
// Media3
// -------------------------------
fun DependencyHandler.media3Player() {
    implementation(Libs.Media3.exoplayer)
    implementation(Libs.Media3.ui)
    implementation(Libs.Media3.exoplayerHls)
    implementation(Libs.Media3.exoplayerDash)
    implementation(Libs.Media3.uiCompose)
    implementation(Libs.Media3.datasource)
    implementation(Libs.Media3.datasourceOkhttp)
    implementation(Libs.Media3.database)
    implementation(Libs.Media3.session)
    implementation(Libs.Media3.workManager)
    implementation(Libs.Media3.cast)
}
