package com.tzh.mamp.provider

import androidx.annotation.StringRes
import java.io.InputStream

interface ResourceProvider {
    fun getString(@StringRes id: Int): String
    fun openAssetFile(fileName: String): InputStream
}