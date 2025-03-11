package com.example.buttraking.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircularProgressViewTotalStudents @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null

) : View(context, attrs) {
    private lateinit var name:String

    private val backgroundPaint = Paint().apply {
        color = 0xFFE0E0E0.toInt() // Light gray
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    fun setCircleName(Cname: String){
        name = Cname
    }

    private val progressPaint = Paint().apply {
        color = 0xFF7494fb.toInt() // Blue
        style = Paint.Style.STROKE
        strokeWidth = 20f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = 0xFF000000.toInt() // Black text color
        textSize = 50f
        isAntiAlias = true
        isFakeBoldText =true
        textAlign = Paint.Align.CENTER
    }

    private val textPresent = Paint().apply {
        color = 0xFF000000.toInt() // Black text color
        textSize = 30f
        isAntiAlias = true
        isFakeBoldText =true
        textAlign = Paint.Align.CENTER
    }

    private var progress: Float = 0f // Progress value (0 to 100)

    private var text: String = "" // Text to display in the center

    private val textTopMargin = 24f



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = minOf(centerX, centerY) - 20f // Adjust for stroke width

        // Draw the background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw the progress circle (sweep angle is based on progress)
        val sweepAngle = (progress / 100f) * 360f
        canvas.drawArc(
            centerX - radius,
            centerY - radius,
            centerX + radius,
            centerY + radius,
            90f, // Start angle (top of the circle)
            sweepAngle, // Sweep angle
            false, // Do not use the center
            progressPaint
        )

        // Draw the text in the middle of the circle
        canvas.drawText(text, centerX, centerY + (textPaint.textSize / 3), textPaint)

        // Draw the "Leave" label below the percentage
        canvas.drawText("", centerX, centerY + textPresent.textSize+textTopMargin, textPresent)
    }

    // Method to update progress
    fun setProgress(progress: Float) {
        this.progress = progress
        text = "${progress.toInt()}%"
        // Update the text to show the current progress
        invalidate() // Redraw the view
    }
}
