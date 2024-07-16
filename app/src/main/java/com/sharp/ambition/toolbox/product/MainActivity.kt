package com.sharp.ambition.toolbox.product

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson
import com.sharp.ambition.frame.startActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityMainBinding
import com.sharp.ambition.toolbox.product.app.AppListActivity
import com.sharp.ambition.toolbox.product.develop.TopActivityManager
import com.sharp.ambition.toolbox.product.image.qrcode.GenerateQrcodeActivity
import com.sharp.ambition.toolbox.product.image.qrcode.QrcodeFinderActivity
import com.sharp.ambition.toolbox.product.server.ServerSocketService
import com.sharp.ambition.toolbox.product.webview.WebViewActivity
import com.zy.devicelibrary.UtilsApp
import com.zy.devicelibrary.data.HardwareData

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.setDecorFitsSystemWindows(true)
//        window.statusBarColor = Color.RED

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter = object : BaseQuickAdapter<FuncItem, BaseViewHolder>(R.layout.item_main_func) {

            override fun convert(holder: BaseViewHolder, item: FuncItem) {
                holder.setText(R.id.tv_title, item.title)
                    .setImageResource(R.id.iv_logo, item.imageRes)
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            if (position == 0) {
                startActivity<GenerateQrcodeActivity>()
            } else if (position == 1) {
                startActivity<QrcodeFinderActivity>()
            } else if (position == 2) {
                TopActivityManager.startFloatingService(this)
            } else if (position == 3) {
                startActivity<AppListActivity>()
            } else if (position == 4) {
                startActivity<WebViewActivity>()
            } else if (position == 5) {
                startActivity<RulerActivity>()
            }
        }
        binding.rvFunc.adapter = adapter

        val funcList = arrayListOf(
            FuncItem("生成二维码", R.drawable.ic_qrcode),
            FuncItem("扫描二维码", R.drawable.ic_scan_qrcode),
            FuncItem("顶部Activity", R.drawable.ic_app_develop),
            FuncItem("手机应用列表", R.drawable.ic_app_list),
            FuncItem("WebView", R.drawable.ic_app_list),
            FuncItem("尺子", R.drawable.ic_app_list)
        )
        adapter.setList(funcList)
        startService(Intent(this, ServerSocketService::class.java))
        UtilsApp.init(application)
        Log.e("TAG", "PRODUCT " + android.os.Build.HARDWARE)
        val hardwareData = HardwareData()
        Log.e("TAG", GsonUtils.toJson(hardwareData));
    }

    data class FuncItem(val title: String, @DrawableRes val imageRes: Int)

}