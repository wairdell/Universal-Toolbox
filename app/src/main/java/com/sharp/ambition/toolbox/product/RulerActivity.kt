package com.sharp.ambition.toolbox.product

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnSystemUiVisibilityChangeListener
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sharp.ambition.toolbox.R


/**
 *    author : fengqiao
 *    date   : 2023/10/7 17:53
 *    desc   :
 */
class RulerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        val window = window
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val attributes = window.attributes
        window.decorView.setBackgroundColor(0)
        if (Build.VERSION.SDK_INT >= 26) {
            try {
                val declaredField =
                    attributes.javaClass.getDeclaredField("layoutInDisplayCutoutMode")
                declaredField.isAccessible = true
                declaredField.set(attributes, 1)
                window.attributes = attributes
            } catch (e8: Exception) {
                Log.i("RulerActivity", e8.toString())
            }
        }
        setContentView(R.layout.activity_ruler)
        window.decorView.setOnSystemUiVisibilityChangeListener {
            getWindow().decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

    }

}