package com.tzh.mamp.provider

import android.content.Context
import java.io.InputStream

class AppResourceProvider(private val context: Context) : ResourceProvider {
    override fun getString(id: Int): String {
        return context.getString(id)
    }

    override fun openAssetFile(fileName: String): InputStream = context.assets.open(fileName)
}