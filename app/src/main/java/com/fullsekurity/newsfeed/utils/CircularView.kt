package com.fullsekurity.newsfeed.utils

import android.content.Context
import android.graphics.*
import android.view.View
import com.fullsekurity.newsfeed.logger.LogUtils


class CircularView(ctxt: Context) : View(ctxt) {

    val bitmap: Bitmap = Bitmap.createBitmap(240, 240, Bitmap.Config.ARGB_8888)
    val newCanvas = Canvas(bitmap)
    private val path = Path()
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w: Float = width.toFloat()
        val h: Float = height.toFloat()
        LogUtils.D("JIMX", LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format(">>>>>>  %d   %d", width, height))
        val radius = if (w > h) h / 4 else w / 4
        path.addCircle(w / 2, h / 2, radius, Path.Direction.CW)
        paint.color = Color.BLACK
        paint.strokeWidth = 20f
        paint.style = Paint.Style.FILL
        val oval = RectF()
        paint.style = Paint.Style.STROKE
        val centerX = w / 2
        val centerY = h / 2
        oval[centerX - radius, centerY - radius, centerX + radius] = centerY + radius
        canvas.drawArc(oval, 90f, 180f, false, paint)
        newCanvas.drawArc(oval, 90f, 180f, false, paint)
    }

}