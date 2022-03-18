package com.sharp.ambition.toolbox.product.develop

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.*
import android.widget.TextView
import com.sharp.ambition.toolbox.R
import kotlin.math.abs

/**
 *    author : fengqiao
 *    date   : 2021/11/29 10:43
 *    desc   :
 */
@SuppressLint("ClickableViewAccessibility")
class FloatingWindowHelper(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val rootView: View =
        LayoutInflater.from(context).inflate(R.layout.layout_floating, null)

    private var lastX = 0F
    private var lastY = 0F

    init {
        rootView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val configuration = ViewConfiguration.get(context)
                    if (abs(event.rawX - lastX) >= configuration.scaledTouchSlop || abs(event.rawY - lastY) >= configuration.scaledEdgeSlop) {
                        layoutParams.x = (layoutParams.x + (event.rawX - lastX)).toInt()
                        layoutParams.y = (layoutParams.y + (event.rawY - lastY)).toInt()
                        windowManager.updateViewLayout(rootView, layoutParams)
                        lastX = event.rawX
                        lastY = event.rawY
                    }
                }
            }
            return@setOnTouchListener true
        }
    }

    private lateinit var layoutParams: WindowManager.LayoutParams

    fun showFloatingWindow() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)) {
            layoutParams = WindowManager.LayoutParams().apply {
                type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_PHONE
                }
                format = PixelFormat.RGBA_8888
                gravity = Gravity.START or Gravity.TOP
                flags =
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = WindowManager.LayoutParams.WRAP_CONTENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                x = 0
                y = 0
            }
            windowManager.addView(rootView, layoutParams)
        } else {

        }
    }

    fun updateInfo(classInfo: CharSequence, packageInfo: CharSequence) {
        rootView.findViewById<TextView>(R.id.tv_activity_name).text = classInfo
        rootView.findViewById<TextView>(R.id.tv_package_name).text = packageInfo
        windowManager.updateViewLayout(rootView, layoutParams)
    }


}