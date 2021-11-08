package com.sharp.ambition.toolbox.product.image.qrcode

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sharp.ambition.frame.BaseActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityGenerateQrcodeBinding

/**
 *    author : fengqiao
 *    date   : 2021/11/8 14:30
 *    desc   :
 */
class GenerateQrcodeActivity : BaseActivity() {

    private lateinit var binding : ActivityGenerateQrcodeBinding

    private val viewModel by viewModels<GenerateQrcodeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.qrcodeBitmap.observe(this) {
            binding.ivQrcode.setImageBitmap(it)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_generate_qrcode)
        binding.btnGenerate.setOnClickListener {
            val content = binding.etContent.text.toString()
            viewModel.generateQrcode(content)
        }
    }

}