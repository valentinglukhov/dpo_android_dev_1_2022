package com.example.views_and_animations.ui.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class Clock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var clockHeight = 0f
    private var clockWidth = 0f
    private var padding = 0f
    private var numeralSpacing = 0f
    private var handTruncation = 0f
    private var hourHandTruncation = 0f
    private var radius = 0f
    private val rectangle = Rect()
    lateinit var paint: Paint
    private var isInit = false
    private val clockHours = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private var timeInMillis: Long? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val calendar = Calendar.getInstance()
        if (timeInMillis != null) {
            calendar.timeInMillis = timeInMillis!!.toLong()
            timeInMillis = null
        }
        if (!isInit) {
            paint = Paint()
            clockHeight = height.toFloat()
            clockWidth = width.toFloat()
            padding = numeralSpacing + 50
            val minAttribute = min(clockHeight, clockWidth)
            radius = minAttribute / 2 - padding
            handTruncation = minAttribute / 20
            hourHandTruncation = minAttribute / 17
            isInit = true
        }
        canvas?.drawColor(Color.DKGRAY)

        paint.reset()
        paint.color = Color.WHITE
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 7f
        paint.isAntiAlias = true

        canvas?.drawCircle(clockWidth / 2, clockHeight / 2, radius + padding - 10f, paint)
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(clockWidth / 2, clockHeight / 2, 27f, paint)
        val fontSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
        paint.textSize = fontSize
        for (hour in clockHours) {
            paint.getTextBounds(hour.toString(), 0, hour.toString().length, rectangle)
            val angle = Math.PI / 6 * (hour - 3)
            val x = (clockWidth / 2 + cos(angle) * radius - rectangle.width() / 2)
            val y = (clockHeight / 2 + sin(angle) * radius + rectangle.height() / 2)
            canvas?.drawText(hour.toString(), x.toFloat(), y.toFloat(), paint)
        }

        var hour = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        drawHandLine(
            canvas,
            (hour + calendar.get(Calendar.MINUTE) / 60).toDouble() * 5f,
            true,
            false
        )
        drawHandLine(canvas, calendar.get(Calendar.MINUTE).toDouble(), false, false)
        drawHandLine(canvas, calendar.get(Calendar.SECOND).toDouble(), false, true)
    }

    fun invalidateClock(timeState: TimeState) {
        timeInMillis = timeState.timeStamp
        invalidate()
    }

    fun drawHandLine(canvas: Canvas?, timestamp: Double, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * timestamp / 30 - Math.PI / 2
        val handRadius = if (isHour) {
            radius - handTruncation - hourHandTruncation
        } else {
            radius - handTruncation
        }
        if (isSecond) paint.color = Color.YELLOW
        canvas?.drawLine(
            clockWidth / 2,
            clockHeight / 2,
            (clockWidth / 2 + cos(angle) * handRadius).toFloat(),
            (clockHeight / 2 + sin(angle) * handRadius).toFloat(),
            paint
        )
    }
}