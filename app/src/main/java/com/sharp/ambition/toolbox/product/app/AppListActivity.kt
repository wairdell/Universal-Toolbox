package com.sharp.ambition.toolbox.product.app

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sharp.ambition.toolbox.R
import com.sharp.ambition.toolbox.databinding.ActivityAppListBinding
import com.sharp.ambition.viewScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 *    author : fengqiao
 *    date   : 2021/12/21 11:16
 *    desc   :
 */
class AppListActivity: AppCompatActivity() {

    private lateinit var binding : ActivityAppListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_list)
        val adapter = Adapter()
        adapter.setOnItemClickListener { _, view, position ->
            startActivity(Intent(this, AppDetailsActivity::class.java).apply {
                putExtra(AppDetailsActivity.EXTRA_INFO, adapter.getItem(position))
            })
        }
        binding.recyclerView.adapter = adapter
        adapter.data = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
    }

    class Adapter : BaseQuickAdapter<PackageInfo, BaseViewHolder>(R.layout.item_app) {

        override fun convert(holder: BaseViewHolder, item: PackageInfo) {

            holder.itemView.viewScope.launch {
                val appInfo = withContext(Dispatchers.IO) {
                    AppInfo(item.applicationInfo.loadLabel(context.packageManager), item.packageName, "${item.versionName}(${item.versionCode})", item.applicationInfo.loadIcon(context.packageManager))
                }
                holder.setText(R.id.tv_app_name, appInfo.appName)
                    .setText(R.id.tv_package_name, appInfo.packageName)
                    .setText(R.id.tv_version, appInfo.version)
                holder.getView<ImageView>(R.id.iv_icon).load(appInfo.icon)

            }

        }
    }

    data class AppInfo(val appName: CharSequence, val packageName: String, val version: String, val icon: Drawable)

}