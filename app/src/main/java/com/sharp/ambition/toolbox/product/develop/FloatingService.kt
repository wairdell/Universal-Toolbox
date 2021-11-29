package com.sharp.ambition.toolbox.product.develop

import android.app.ActivityManager
import android.app.Service
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.WindowManager
import android.view.Gravity

import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sharp.ambition.toolbox.R
import java.util.*


/**
 *    author : fengqiao
 *    date   : 2021/11/9 9:38
 *    desc   :
 */
class FloatingService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showFloatingWindow();
        return super.onStartCommand(intent, flags, startId)
    }

    private fun showFloatingWindow() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val layoutParams = WindowManager.LayoutParams().apply {
                type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_PHONE
                }
                format = PixelFormat.RGBA_8888
                gravity = Gravity.LEFT or Gravity.TOP
                flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = 400
                height = 200
                x = 0
                y = 0
            }
            var rootView = LayoutInflater.from(this).inflate(R.layout.layout_floating, null)
            rootView.findViewById<TextView>(R.id.tv_activity_name).text = getTopActivityInfo()
            windowManager.addView(rootView, layoutParams)
            updateInfo(windowManager, rootView, layoutParams)
        } else {

        }
    }

    fun updateInfo(wm: WindowManager, rootView: View, layoutParams: WindowManager.LayoutParams) {
        handler.postDelayed({
            rootView.findViewById<TextView>(R.id.tv_activity_name).text = getTopActivityInfo()
            wm.updateViewLayout(rootView, layoutParams)
            updateInfo(wm, rootView, layoutParams)
        }, 200)
    }

    fun getTopActivityInfo(): String {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return am.appTasks[0].taskInfo.topActivity?.className ?: "Unknown"
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val mUsageStatsManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager;
            val time: Long = System.currentTimeMillis();
            // We get usage stats for the last 10 seconds
            val stats: List<UsageStats>? = mUsageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 10,
                time
            )
            // Sort the stats by the last time used
            if (stats != null) {
                val sortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
                for (usageStats in stats) {
                    sortedMap[usageStats.lastTimeUsed] = usageStats
                }
                if (!sortedMap.isEmpty()) {
                    return sortedMap[sortedMap.lastKey()]?.packageName ?: "Unknown"
                }
            }
        }  else {
            var am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return am.getRunningTasks(1)[0].topActivity?.className ?: "Unknown"
        }
        return "Unknown";
    }


}