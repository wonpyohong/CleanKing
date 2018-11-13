package com.wonpyohong.android.cleanking.calendar.month

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.RelativeLayout
import com.wonpyohong.android.cleanking.ui.selectedDate

class DayOfMonth(val context: Context, val firstDateInMonth: LocalDate, val date: LocalDate) {
    var xStart = 0f
    var yStart = 0f
    var width = 0f
    var height = 0f

    val paint = Paint()

    fun draw(canvas: Canvas) {
        drawDate(canvas)
        drawCalorie(canvas)
    }

    private fun drawDate(canvas: Canvas) {
        paint.textSize = 32f

        paint.alpha = if (firstDateInMonth.monthValue == date.monthValue) 255 else 50

        val isToday = date == LocalDate.now()
        if (isToday) {
            setColorWithAlpha(paint, if (date.dayOfWeek == DayOfWeek.SUNDAY) Color.RED else Color.BLACK)
            canvas.drawCircle(xStart + 20, yStart - 10, 30f, paint)

            setColorWithAlpha(paint, Color.WHITE)
            canvas.drawText("${date.dayOfMonth}", xStart, yStart, paint)
        } else {
            val isSelectedDay = date == selectedDate
            if (isSelectedDay) {
                setColorWithAlpha(paint, Color.GRAY)
                canvas.drawCircle(xStart + 20, yStart - 10, 30f, paint)

                setColorWithAlpha(paint, if (date.dayOfWeek == DayOfWeek.SUNDAY) Color.RED else Color.BLACK)
                canvas.drawText("${date.dayOfMonth}", xStart, yStart, paint)
            } else {
                setColorWithAlpha(paint, if (date.dayOfWeek == DayOfWeek.SUNDAY) Color.RED else Color.BLACK)
                canvas.drawText("${date.dayOfMonth}", xStart, yStart, paint)
            }
        }
    }

    private fun drawCalorie(canvas: Canvas) {
    }

    private fun setColorWithAlpha(paint: Paint, color: Int) {
        paint.color = color and ((paint.alpha shl 24) or 0xFFFFFF)
    }

    fun createViewForDragging(): ImageView {
        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        canvas.translate(-xStart, -yStart + 50)
        draw(canvas)
        val imageView = ImageView(context)
        imageView.setImageBitmap(bitmap)

        imageView.layoutParams = RelativeLayout.LayoutParams(width.toInt(), height.toInt())

        return imageView
    }
}