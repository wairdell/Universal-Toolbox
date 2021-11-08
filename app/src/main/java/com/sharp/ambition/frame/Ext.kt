package com.sharp.ambition.frame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import com.google.zxing.WriterException

import com.google.zxing.BarcodeFormat

import com.google.zxing.MultiFormatWriter

import com.google.zxing.common.BitMatrix

import com.google.zxing.EncodeHintType
import java.util.*
import kotlin.collections.HashMap


/**
 *    author : fengqiao
 *    date   : 2021/11/8 15:11
 *    desc   :
 */

inline fun <reified T : Activity> Context.startActivity(noinline block: Intent.() -> Unit = {}) {
    startActivity(Intent(this, T::class.java).apply(block))
}

const val PADDING_SIZE_MIN = 0

suspend fun String.createQrcode(widthAndHeight: Int): Bitmap {
    return withContext(Dispatchers.IO) {
        val hints: MutableMap<EncodeHintType, String> = HashMap<EncodeHintType, String>()
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        var matrix: BitMatrix? = null
        matrix = MultiFormatWriter().encode(
            this@createQrcode,
            BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight, hints
        )
        val width = matrix.width
        val height = matrix.height
        val pixels = IntArray(width * height)
        var isFirstBlackPoint = false
        var startX = 0
        var startY = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (matrix[x, y]) {
                    if (!isFirstBlackPoint) {
                        isFirstBlackPoint = true
                        startX = x
                        startY = y
                        Log.d("createQRCode", "x y = $x $y")
                    }
                    pixels[y * width + x] = -0x1000000
                }
            }
        }
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

        // 剪切中间的二维码区域，减少padding区域
        if (startX <= PADDING_SIZE_MIN) {
            bitmap
        } else {
            val x1: Int = startX - PADDING_SIZE_MIN
            val y1: Int = startY - PADDING_SIZE_MIN
            if (x1 < 0 || y1 < 0) {
                bitmap
            } else {
                val w1 = width - x1 * 2
                val h1 = height - y1 * 2
                val bitmapQR: Bitmap = Bitmap.createBitmap(bitmap, x1, y1, w1, h1)
                bitmapQR
            }
        }

    }
}
