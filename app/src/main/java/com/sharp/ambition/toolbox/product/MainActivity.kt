package com.sharp.ambition.toolbox.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sharp.ambition.frame.startActivity
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityMainBinding
import com.sharp.ambition.toolbox.product.image.qrcode.GenerateQrcodeActivity
import com.sharp.ambition.toolbox.product.image.qrcode.ScanQrcodeActivity

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
            } else {
                startActivity<ScanQrcodeActivity>()
            }
        }
        binding.rvFunc.adapter = adapter

        val funcList = arrayListOf(
            FuncItem("生成二维码", R.drawable.ic_qrcode),
            FuncItem("扫描二维码", R.drawable.ic_qrcode)
        )
        adapter.setList(funcList)
    }

    data class FuncItem(val title: String, @DrawableRes val imageRes: Int)

}