package com.sharp.ambition.toolbox.product.image.qrcode

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import coil.load
import com.sharp.ambition.frame.BaseActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityQrcodeFinderBinding
import java.io.File

/**
 *    author : fengqiao
 *    date   : 2021/11/8 18:35
 *    desc   :
 */
class QrcodeFinderActivity : BaseActivity() {

    private lateinit var permissionResultLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityQrcodeFinderBinding>(this, R.layout.activity_qrcode_finder)
        val resultLauncher = registerForActivityResult(ScanQrcodeActivity.ResultContract()) {
                it?.let { data ->
                    binding.etResult.setText(data.text)
                    binding.ivQrcode.load(File(data.bitmapPath))
                }
            }
        binding.btnConfirm.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
            } else {
                resultLauncher.launch(null)
            }
        }
        binding.btnOpen.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.etResult.text.toString()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "没有应用支持打开此链接", Toast.LENGTH_LONG).show()
            }
        }
        permissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                resultLauncher.launch(null)
            }
        }
        if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionResultLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            resultLauncher.launch(null)
        }
    }

}