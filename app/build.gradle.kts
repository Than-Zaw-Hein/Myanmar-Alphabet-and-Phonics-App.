import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.gms.google.services)
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")

}

android {
    namespace = "com.tzh.mamp"

    compileSdk = ProjectConfig.COMPILE_SDK
    // Read local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }
    defaultConfig {
        minSdk = ProjectConfig.MIN_SDK
        targetSdk = ProjectConfig.TARGET_SDK
        applicationId = ProjectConfig.APPLICATION_ID
        versionCode = ProjectConfig.VersionCode
        versionName = ProjectConfig.VersionName
        multiDexEnabled = true
        testInstrumentationRunner = ProjectConfig.AndroidJunitRunner

        vectorDrawables {
            useSupportLibrary = true
        }
        signingConfig = signingConfigs.getByName("debug")
//        manifestPlaceholders["com.google.android.gms.ads.APPLICATION_ID"] =
//            "ca-app-pub-5023647269799812~1361514940"
//        signingConfig = signingConfigs.getByName("release")
        buildConfigField("String", "YOUTUBE_API_KEY", "\"${localProperties.getProperty("YOUTUBE_API_KEY")}\"")

    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField(
                "String",
                "HOMESCREEN_ADS_KEY",
                "\"ca-app-pub-5023647269799812/5911860399\""
            )
            buildConfigField(
                "String",
                "VOWEL_ADS_KEY",
                "\"ca-app-pub-5023647269799812/7689752590\""
            )
            buildConfigField(
                "String",
                "TRACING_ADS_KEY",
                "\"ca-app-pub-5023647269799812/4269382334\""
            )
            buildConfigField(
                "String",
                "DETAIL_ADS_KEY",
                "\"ca-app-pub-5023647269799812/8017055657\""
            )
            buildConfigField(
                "String",
                "QUIZ_SUCCESS_ADS_KEY",
                "\"ca-app-pub-5023647269799812/3332539331\""
            )
        }
        debug {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "HOMESCREEN_ADS_KEY",
                "\"ca-app-pub-3940256099942544/6300978111\""
            ) // test banner
            buildConfigField(
                "String",
                "VOWEL_ADS_KEY",
                "\"ca-app-pub-3940256099942544/6300978111\""
            )    // also test banner
            buildConfigField(
                "String",
                "TRACING_ADS_KEY",
                "\"ca-app-pub-3940256099942544/6300978111\""
            )    // also test banner
            buildConfigField(
                "String",
                "DETAIL_ADS_KEY",
                "\"ca-app-pub-3940256099942544/6300978111\""
            )    // also test banner
            buildConfigField(
                "String",
                "QUIZ_SUCCESS_ADS_KEY",
                "\"ca-app-pub-3940256099942544/1033173712\""
            )    // also test banner
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions.incremental = false
    applicationVariants.all {
        val variantName = name
        kotlin.sourceSets.named(variantName) {
            kotlin.srcDir("build/generated/ksp/$variantName/kotlin")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.configureEach {
    if (name.startsWith("compile") && name.endsWith("Kotlin")) {
        mustRunAfter(tasks.withType(com.google.devtools.ksp.gradle.KspTask::class))
    }
}
dependencies {
//    implementation(libs.play.services.ads.api)
    //    implementation(libs.firebase.database)
    // --- Modules ---
    useModule(":libraries:framework")
    useModule(":libraries:jetframework")
    supportLib()
    compose()
    // --- Navigation ---
    navigation()
    //firebase
    firebase()

    // --- Accompanist ---
    accompanist()

    //
    network()
    // --- Storage ---
    storage()


    // --- Dependency Injection ---
    hilt()

    //
    media3Player()

    // YouTube Player
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.2")

}