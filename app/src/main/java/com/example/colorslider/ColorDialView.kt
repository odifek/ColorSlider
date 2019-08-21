package com.example.colorslider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


class ColorDialView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    private var dialDrawable: Drawable? = null
    private var dialDiameter = toDp(100)

    // Pre-computed helper values
    private var horizontalSize = 0f
    private var verticalSize = 0f

    // Pre-computed position values
    private var centerHorizontal = 0f
    private var centerVertical = 0f


    init {
        dialDrawable = context.getDrawable(R.drawable.ic_dial).also {
            it?.bounds = getCenteredBounds(dialDiameter)
            it?.setTint(Color.DKGRAY)
        }
        refreshValues()
    }

    private fun getCenteredBounds(size: Int, scalar: Float = 1f): Rect {
        val half = ((if (size >= 0) size / 2 else 1) * scalar).toInt()
        return Rect(-half, -half, half, half)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(centerHorizontal, centerVertical)
        dialDrawable?.draw(canvas)

    }

    fun refreshValues() {
        horizontalSize = dialDiameter.toFloat()
        verticalSize = dialDiameter.toFloat()

        centerHorizontal = horizontalSize / 2f
        centerVertical = verticalSize / 2f
    }

    private fun toDp(value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), context.resources.displayMetrics).toInt()
    }
}
