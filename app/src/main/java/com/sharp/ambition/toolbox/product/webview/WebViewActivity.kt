package com.sharp.ambition.toolbox.product.webview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.coroutineScope
import com.sharp.ambition.frame.BaseActivity
import com.sharp.ambition.generateBitmap
import com.sharp.ambition.generateFile
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityWebViewBinding
import kotlinx.coroutines.launch

/**
 * author : fengqiao
 * date   : 2022/5/27 10:56
 * desc   :
 */
@SuppressLint("SetJavaScriptEnabled")
class WebViewActivity : BaseActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                textZoom = 100
                setSupportZoom(true)
                useWideViewPort = true
                loadWithOverviewMode = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                javaScriptCanOpenWindowsAutomatically = true
                setNeedInitialFocus(false)
            }
            webChromeClient
            webViewClient = object : WebViewClient() {

                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    Log.e("TAG", "shouldOverrideUrlLoading: " + request.url.toString() )
                    return if (URLUtil.isNetworkUrl(request.url.toString())) {
                        return super.shouldOverrideUrlLoading(view, request)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
                        if (intent.resolveActivity(packageManager) != null) {
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            true
                        } else {
                            super.shouldOverrideUrlLoading(view, request)
                        }
                    }
                }

                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    Log.e("TAG", "onPageFinished = $url");
                }
            }
            loadUrl("http://10.10.30.79:8080/#/aliMiniPrograms?k=eyJ1IjozNTExMTY1MSwibCI6MjY1MTcwMywidCI6MTY5MzI5Nzc3OX0%3D")
        }
        binding.btnGenerate.setOnClickListener {
            lifecycle.coroutineScope.launch {
                binding.webView.generateBitmap().generateFile(this@WebViewActivity)
            }
        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return
        }
        super.onBackPressed()
    }

}