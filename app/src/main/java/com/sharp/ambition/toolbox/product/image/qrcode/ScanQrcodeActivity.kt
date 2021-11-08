package com.sharp.ambition.toolbox.product.image.qrcode

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.view.SurfaceHolder
import androidx.databinding.DataBindingUtil
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.sharp.ambition.frame.BaseActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityScanQrcodeBinding
import com.sharp.ambition.zxing.ProxyView
import com.sharp.ambition.zxing.camera.CameraManager
import com.sharp.ambition.zxing.decoding.CaptureActivityHandler
import com.sharp.ambition.zxing.decoding.InactivityTimer
import com.sharp.ambition.zxing.view.ViewfinderView
import java.util.*

/**
 *    author : fengqiao
 *    date   : 2021/11/8 17:21
 *    desc   :
 */
class ScanQrcodeActivity : BaseActivity(), ProxyView, SurfaceHolder.Callback {

    companion object {

        const val EXTRA_RESULT_TEXT = "EXTRA_RESULT_TEXT"

    }

    private lateinit var binding: ActivityScanQrcodeBinding
    private var hasSurface = false
    private val inactivityTimer: InactivityTimer by lazy { InactivityTimer(this) }
    private var handler: CaptureActivityHandler? = null
    private var decodeFormats: Vector<BarcodeFormat>? = null
    private var characterSet: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_qrcode)
        CameraManager.init(this)
        hasSurface = false
    }


    override fun onResume() {
        super.onResume()
        val surfaceHolder = binding.previewView.holder
        if (hasSurface) {
            initCamera(surfaceHolder)
        } else {
            surfaceHolder.addCallback(this)
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
        decodeFormats = null
        characterSet = "ISO-8859-1"
    }

    override fun onPause() {
        super.onPause()
        handler?.quitSynchronously()
        handler = null
        CameraManager.get().closeDriver()
        if(!hasSurface) {
            binding.previewView.holder.removeCallback(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inactivityTimer.shutdown()
    }

    private fun initCamera(surfaceHolder: SurfaceHolder) {
        CameraManager.get().openDriver(surfaceHolder);
        if (handler == null) {
            handler = CaptureActivityHandler(this, this, decodeFormats, characterSet)
        }

    }

    override fun getHandler(): Handler? {
        return handler
    }

    override fun getViewfinderView(): ViewfinderView {
        return binding.viewfinderView
    }

    override fun handleDecode(result: Result, barcode: Bitmap) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(EXTRA_RESULT_TEXT, result.text)
        })
    }

    override fun drawViewfinder() {
        viewfinderView.drawViewfinder()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        if (!hasSurface) {
            hasSurface = true
            initCamera(holder)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurface = false
    }

}