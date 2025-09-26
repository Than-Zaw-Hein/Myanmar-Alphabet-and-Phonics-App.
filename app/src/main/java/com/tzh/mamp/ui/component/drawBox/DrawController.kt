package com.tzh.mamp.ui.component.drawBox

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.UUID
import androidx.core.graphics.createBitmap

class DrawController internal constructor() {
    internal var strokeWidth = 5f
    internal var strokeColor = Color.Red
    internal val undoStack = ArrayList<PathWrapper>()
    internal val redoStack = ArrayList<PathWrapper>()

    private var bitmap: Bitmap? = null


    fun importPath(path: ArrayList<PathWrapper>) {
        reset()
        undoStack.addAll(path)
        bitmap?.eraseColor(android.graphics.Color.TRANSPARENT)
        emit("${undoStack.size}")
    }

    fun exportPath() = undoStack


    fun setStrokeColor(color: Color) {
        strokeColor = color
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth = width
    }

    fun unDo() {
        if (undoStack.isNotEmpty()) {
            val last = undoStack.last()
            redoStack.add(last)
            undoStack.remove(last)
            bitmap?.eraseColor(android.graphics.Color.TRANSPARENT)
        }
        emit("${undoStack.size}")
    }

    fun reDo() {
        if (redoStack.isNotEmpty()) {
            val last = redoStack.last()
            undoStack.add(last)
            redoStack.remove(last)
            bitmap?.eraseColor(android.graphics.Color.TRANSPARENT)
        }
        emit("${undoStack.size}")
    }

    fun reset() {
        redoStack.clear()
        undoStack.clear()
        bitmap?.eraseColor(android.graphics.Color.TRANSPARENT)
        emit(UUID.randomUUID().toString())
    }


    fun getDrawBoxBitmap() = bitmap


    internal fun generateCanvas(size: IntSize): Canvas? = if (size.width > 0 && size.height > 0) {
        bitmap = createBitmap(size.width, size.height)
        Canvas(bitmap!!.asImageBitmap())
    } else null


    private val _changeRequests = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val changeRequests = _changeRequests.asSharedFlow()

    fun emit(state: String = "") {
        _changeRequests.tryEmit(state)
    }

}

@Composable
fun rememberDrawController(): DrawController {
    return remember { DrawController() }
}