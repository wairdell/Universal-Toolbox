package com.sharp.ambition.toolbox.product.app

import android.content.pm.PackageInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ClipboardUtils
import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityAppDetailsBinding
import com.sharp.ambition.viewScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *    author : fengqiao
 *    date   : 2021/12/21 13:59
 *    desc   :
 */
class AppDetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_INFO = "EXTRA_INFO"
    }

    private lateinit var binding : ActivityAppDetailsBinding
    private lateinit var packageInfo: PackageInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        packageInfo = intent.getParcelableExtra<PackageInfo>(EXTRA_INFO)!!
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_details)
        binding.ivIcon.viewScope.launch {
            binding.ivIcon.load(withContext(Dispatchers.IO) {
                packageInfo.applicationInfo.loadIcon(this@AppDetailsActivity.packageManager)
            })
        }
        binding.tvAppName.viewScope.launch {
            binding.tvAppName.text = withContext(Dispatchers.IO) {
                packageInfo.applicationInfo.loadLabel(this@AppDetailsActivity.packageManager)
            }
        }
        binding.ivIcon.setOnLongClickListener {
            ClipboardUtils.copyText(GsonUtils.toJson(packageInfo))
            true
        }
        val adapter = Adapter()
        binding.recyclerView.adapter = adapter
        val detailsFuncList = mutableListOf<DetailsFuncItem>()
        detailsFuncList.add(DetailsFuncItem("签名 SHA1") {
            return@DetailsFuncItem AppUtils.getAppSignaturesSHA1(packageInfo.packageName).reduce { acc, i -> "$acc,$i" }
        })
        detailsFuncList.add(DetailsFuncItem("签名 MD5") {
            return@DetailsFuncItem AppUtils.getAppSignaturesMD5(packageInfo.packageName).reduce { acc, i -> "$acc,$i" }
        })
        detailsFuncList.add(DetailsFuncItem("签名 SHA256") {
            return@DetailsFuncItem AppUtils.getAppSignaturesSHA256(packageInfo.packageName).reduce { acc, i -> "$acc,$i" }
        })
        detailsFuncList.add(DetailsFuncItem("targetSdkVersion") {
            return@DetailsFuncItem packageInfo.applicationInfo.targetSdkVersion.toString()
        })
        adapter.setNewInstance(detailsFuncList)
        /*detailsFuncList.add(DetailsFuncItem("requestedPermissions") {
            return@DetailsFuncItem packageInfo.requestedPermissions?.reduce { acc, s -> "$acc\n$s" } ?: ""
        })*/
    }

    class Adapter : BaseQuickAdapter<DetailsFuncItem, BaseViewHolder>(R.layout.item_app_details) {

        override fun convert(holder: BaseViewHolder, item: DetailsFuncItem) {
            holder.itemView.viewScope.launch {
                val content = withContext(Dispatchers.IO) {
                    item.content.invoke()
                }
                holder.setText(R.id.tv_title, item.title)
                    .setText(R.id.tv_content, content)
            }
        }

    }

    data class DetailsFuncItem(val title: String, val content: () -> CharSequence)

}