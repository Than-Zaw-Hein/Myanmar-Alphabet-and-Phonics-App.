object ProjectConfig {
    const val COMPILE_SDK = 35
    const val MIN_SDK = 26
    const val TARGET_SDK = 35
//    const val extensionVersion = "1.4.6"

    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 1
    private const val versionQualifier = "alpha1"

    private fun generateVersionCode(): Int {
        return versionMajor * 10000 + versionMinor * 100 + versionPatch
    }

    private fun generateVersionName(): String {
        return "$versionMajor.$versionMinor.${versionPatch}"
    }

    const val APPLICATION_ID = "com.tzh.mamp"
    val VersionCode = generateVersionCode()
    val VersionName = generateVersionName()
    const val AndroidJunitRunner = "androidx.test.runner.AndroidJUnitRunner"
    val FreeCompilerArgs = listOf(
        "-Xjvm-default=all",
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=kotlin.Experimental",
        "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.InternalCoroutinesApi",
        "-opt-in=kotlinx.coroutines.FlowPreview",
        "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
        "-opt-in=com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi",
        "-opt-in=androidx.compose.animation.ExperimentalAnimationApi"
        )

        object Release {
            const val BASE_URL = "http://192.168.1.11/movieSystem/"
            const val DB_NAME = "VideoManagementSystem.db"
        }

        object Debug {
            const val BASE_URL = "http://192.168.1.11/movieSystem/"

            const val DB_NAME = "VideoManagementSystem.db"
    }

}