package com.sharp.ambition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

/**
 *    author : fengqiao
 *    date   : 2021/11/29 13:59
 *    desc   :
 */

@Throws(IOException::class)
suspend fun Bitmap.generateFile(file: File) {
    withContext(Dispatchers.IO) {
        kotlin.runCatching {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        }.onFailure {
            if (it is IOException) {
                throw it
            }
        }
    }
}

@Throws(IOException::class)
suspend fun Bitmap.generateFile(context: Context): File {
    val file = File(context.cacheDir.absolutePath, "${UUID.randomUUID()}_bitmap.jpg")
    generateFile(file)
    return file
}

fun View.generateBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    draw(canvas)
    return bitmap
}