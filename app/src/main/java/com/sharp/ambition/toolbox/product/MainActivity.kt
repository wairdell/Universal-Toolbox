package com.sharp.ambition.toolbox.product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sharp.ambition.frame.startActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityMainBinding
import com.sharp.ambition.toolbox.product.app.AppListActivity
import com.sharp.ambition.toolbox.product.develop.TopActivityManager
import com.sharp.ambition.toolbox.product.image.qrcode.GenerateQrcodeActivity
import com.sharp.ambition.toolbox.product.image.qrcode.QrcodeFinderActivity
import com.sharp.ambition.toolbox.product.image.qrcode.ScanQrcodeActivity
import com.sharp.ambition.toolbox.product.server.ServerSocketService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val adapter = object : BaseQuickAdapter<FuncItem, BaseViewHolder>(R.layout.item_main_func) {

            override fun convert(holder: BaseViewHolder, item: FuncItem) {
                holder.setText(R.id.tv_title, item.title)
                    .setImageResource(R.id.iv_logo, item.imageRes)
            }
        }
        adapter.setOnItemClickListener { _, _, position ->
            if(position == 0) {
                startActivity<GenerateQrcodeActivity>()
            } else if (position == 1) {
                startActivity<QrcodeFinderActivity>()
            } else if (position == 2) {
                TopActivityManager.startFloatingService(this)
            } else if(position == 3) {
                startActivity<AppListActivity>()
            }
        }
        binding.rvFunc.adapter = adapter

        val funcList = arrayListOf(
            FuncItem("生成二维码", R.drawable.ic_qrcode),
            FuncItem("扫描二维码", R.drawable.ic_scan_qrcode),
            FuncItem("顶部Activity", R.drawable.ic_app_develop),
            FuncItem("手机应用列表", R.drawable.ic_app_list)
        )
        adapter.setList(funcList)
        startService(Intent(this, ServerSocketService::class.java))
    }

    data class FuncItem(val title: String, @DrawableRes val imageRes: Int)

}