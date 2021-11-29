package com.sharp.ambition.toolbox.product.develop

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast

/**
 *    author : fengqiao
 *    date   : 2021/11/9 9:36
 *    desc   :
 */
object TopActivityManager {

    fun startFloatingService(context: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
            Toast.makeText(context, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
            val uri = Uri.parse("package:" + context.packageName)
            context.startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri), 0)
        } else {
            if (!isAccessibilitySettingsOn(context, MyAccessibilityService::class.java.name)) {
                Toast.makeText(context, "当前无权限，请授权", Toast.LENGTH_SHORT).show()
                jumpToSettingPage(context)
            }
        }
    }

    private fun isAccessibilitySettingsOn(context: Context, className: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        return if (activityManager != null) {
            val runningServices = activityManager.getRunningServices(100) // 获取正在运行的服务列表
            if (runningServices.size < 0) {
                return false
            }
            for (i in runningServices.indices) {
                val service = runningServices[i].service
                if (service.className == className) {
                    return true
                }
            }
            false
        } else {
            false
        }
    }

    private fun jumpToSettingPage(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

}