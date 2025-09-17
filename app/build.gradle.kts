plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.tzh.mamp"

    compileSdk = ProjectConfig.COMPILE_SDK

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
//        signingConfig = signingConfigs.getByName("release")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    // --- Modules ---
    useModule(":libraries:framework")
    useModule(":libraries:jetframework")
    supportLib()
    compose()

    // --- Navigation ---
    navigation()


    // --- Accompanist ---
    accompanist()


    // --- Storage ---
    storage()


    // --- Dependency Injection ---
    hilt()

    //
    media3Player()
}