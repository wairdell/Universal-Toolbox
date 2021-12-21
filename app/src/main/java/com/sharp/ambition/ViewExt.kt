package com.sharp.ambition

import android.view.View
import com.sharp.ambition.toolbox.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

/**
 *    author : fengqiao
 *    date   : 2021/12/21 11:57
 *    desc   :
 */

val View.viewScope : CoroutineScope
    get() {
        val scope: CoroutineScope? = getTag(R.id.tag_view_scope) as? CoroutineScope
        return scope ?: CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).also {
            setTag(R.id.tag_view_scope, it)
            addOnAttachStateChangeListener(it)
        }
    }

internal class CloseableCoroutineScope(context: CoroutineContext) : CoroutineScope, View.OnAttachStateChangeListener  {

    override val coroutineContext: CoroutineContext = context

    override fun onViewAttachedToWindow(v: View) {
    }

    override fun onViewDetachedFromWindow(v: View) {
        coroutineContext.cancel()
        v.setTag(R.id.tag_view_scope, null)
    }

}
