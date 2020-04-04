package com.fullsekurity.newsfeed.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View


class CircularView(
    ctxt: Context,
    private val percentNumeratorOuter: Int,
    private val percentDenominatorOuter: Int,
    private val colorOuter: Int,
    private val percentNumeratorInner: Int,
    private val percentDenominatorInner: Int,
    private val colorInner: Int) : View(ctxt) {

    private val path = Path()
    private val paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // width and height are always equal, so use width as the diameter
        drawCircle(canvas, colorOuter, width.toFloat(), width.toFloat(), percentNumeratorOuter, percentDenominatorOuter)
        drawCircle(canvas, colorInner, width.toFloat(),width.toFloat() * 0.7f, percentNumeratorInner, percentDenominatorInner)
    }

    private fun drawCircle(canvas: Canvas, color: Int, boundingBoxWidth: Float, diameter: Float, percentNumerator: Int, percentDenominator: Int) {
        // the radius is slightly smaller than half the diameter so that the circle does not get clipped by the bounding box
        val radius = 7 * diameter / 16
        path.addCircle(0f, 0f, radius, Path.Direction.CW)
        paint.color = color
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        val centerX = boundingBoxWidth / 2
        val centerY = boundingBoxWidth / 2
        val oval = RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        // right start: 0, bottom start: 90, left start: 180, top start: 270
        // sweep is clockwise
        canvas.drawArc(oval, 270f, 360f * percentNumerator / percentDenominator, false, paint)

    }

}