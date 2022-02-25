package com.obvious.nasaapod.ext

import android.content.res.AssetManager

/**
 * Utility function to read files from assets folder
 */
fun AssetManager.readFile(fileName: String) = open(fileName)
    .bufferedReader()
    .use { it.readText() }