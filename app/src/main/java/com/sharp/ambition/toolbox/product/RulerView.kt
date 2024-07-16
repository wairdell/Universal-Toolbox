package com.sharp.ambition.toolbox.product

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.sharp.ambition.toolbox.R

/**
 *    author : fengqiao
 *    date   : 2023/10/7 17:55
 *    desc   :
 */
class RulerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        linePaint.color = Color.BLACK
        linePaint.textSize = context.resources.getDimension(R.dimen.sp_11)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val lineY = height / 2F
        val lineStart = 0F
        val lineEnd = width.toFloat()
        canvas.drawLine(lineStart, lineY, lineEnd, lineY, linePaint)

        val inUnit = resources.getDimensionPixelOffset(R.dimen.in_1)
        val cmUnit = inUnit / 2.54f
        val mmUnit = cmUnit / 10

        val graduateHeight = cmUnit / 3
        var graduateIndex = 0
        var graduateX = lineStart
        val numStartY = lineY - graduateHeight - linePaint.textSize
        do {
            if (graduateIndex % 10 == 0) {
                canvas.drawLine(graduateX, lineY - graduateHeight, graduateX, lineY, linePaint)
                canvas.drawText((graduateIndex / 10).toString(), graduateX, numStartY, linePaint)
            } else if (graduateIndex % 5 == 0) {
                canvas.drawLine(graduateX, lineY - graduateHeight * 2 / 3, graduateX, lineY, linePaint)
            } else {
                canvas.drawLine(graduateX, lineY - graduateHeight / 4, graduateX, lineY, linePaint)
            }
            graduateX = lineStart + (++graduateIndex) * mmUnit
        } while (graduateX < lineEnd)
    }

}