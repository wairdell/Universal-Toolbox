package com.sharp.ambition.toolbox

import android.app.Application
import com.blankj.utilcode.util.Utils

/**
 *    author : fengqiao
 *    date   : 2021/11/8 14:25
 *    desc   :
 */
class ToolboxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

}