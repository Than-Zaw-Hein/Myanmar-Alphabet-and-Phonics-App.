package com.tzh.mamp.ui.component.drawBox

import android.view.MotionEvent
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
internal fun MotionEvent.getRect() =
    Rect(this.x - 0.5f, this.y - 0.5f, this.x + 0.5f, this.y + 0.5f)

internal fun DrawScope.drawSomePath(
    path: Path,
    color: Color,
    width: Float
) = drawPath(
    path,
    color,
    style = Stroke(width, miter = 0f, join = StrokeJoin.Round, cap = StrokeCap.Round),
)

internal fun Canvas.drawSomePath(
    path: Path,
    color: Color,
    width: Float,
) = this.drawPath(path, Paint().apply {
    this.style = PaintingStyle.Stroke
    this.isAntiAlias = true
    this.color = color
    this.strokeJoin = StrokeJoin.Round
    this.strokeCap = StrokeCap.Round
    this.strokeWidth = width
})


//Model
data class PathWrapper(val path: Path, val strokeWidth: Float = 5f, val strokeColor: Color)