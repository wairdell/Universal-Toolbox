package com.sharp.ambition.toolbox.product.image.qrcode

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sharp.ambition.frame.createQrcode
import kotlinx.coroutines.launch

/**
 *    author : fengqiao
 *    date   : 2021/11/8 17:12
 *    desc   :
 */
class GenerateQrcodeViewModel : ViewModel() {

    val qrcodeBitmap: MutableLiveData<Bitmap> = MutableLiveData();

    fun generateQrcode(content: String) {
        viewModelScope.launch {
            qrcodeBitmap.value = content.createQrcode(200)
        }
    }

}