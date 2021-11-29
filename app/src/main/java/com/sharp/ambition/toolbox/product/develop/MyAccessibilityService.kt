package com.sharp.ambition.toolbox.product.develop

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

/**
 *    author : fengqiao
 *    date   : 2021/11/29 10:20
 *    desc   :
 */
class MyAccessibilityService : AccessibilityService() {

    companion object {
        private val TAG = MyAccessibilityService::class.java.simpleName
    }

    private lateinit var floatingWindowHelper: FloatingWindowHelper

    override fun onCreate() {
        super.onCreate()
        floatingWindowHelper = FloatingWindowHelper(this)
        floatingWindowHelper.showFloatingWindow()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.e(TAG, "onAccessibilityEvent = " + event.className + " , " + event.eventType)
        floatingWindowHelper.updateInfo(event.className,  event.packageName)
    }

    override fun onInterrupt() {

    }
}