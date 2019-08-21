package com.example.colorslider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


class ColorDialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {
    private var colors: ArrayList<Int> =
        arrayListOf(Color.RED, Color.YELLOW, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.GREEN)

    private var dialDrawable: Drawable? = null
    private var dialDiameter = toDp(100)

    private var noColorDrawable: Drawable? = null

    private val paint = Paint().also {
        it.color = Color.BLUE
        it.isAntiAlias = true
    }

    private var extraPadding = toDp(30)
    private var tickSize = toDp(10).toFloat()
    private var angleBetweenColors = 0f

    private var totalLeftPadding = 0f
    private var totalTopPadding = 0f
    private var totalRightPadding = 0f
    private var totalBottomPadding = 0f

    private var tickPositionVertical = 0f

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

        noColorDrawable = context.getDrawable(R.drawable.ic_no_color).also {
            it?.bounds = getCenteredBounds(tickSize.toInt(), 2f)
        }
        colors.add(0, Color.TRANSPARENT)
        angleBetweenColors = 360f / colors.size
        refreshValues()
    }

    private fun getCenteredBounds(size: Int, scalar: Float = 1f): Rect {
        val half = ((if (size >= 0) size / 2 else 1) * scalar).toInt()
        return Rect(-half, -half, half, half)
    }


    override fun onDraw(canvas: Canvas) {
        val saveCount = canvas.save()
        colors.forEachIndexed { i, color ->
            if (i == 0) {
                canvas.translate(centerHorizontal, tickPositionVertical)
                noColorDrawable?.draw(canvas)
                canvas.translate(-centerHorizontal, -tickPositionVertical)
            } else {
                paint.color = colors[i]
                canvas.drawCircle(centerHorizontal, tickPositionVertical, tickSize, paint)
            }
            canvas.rotate(angleBetweenColors, centerHorizontal, centerVertical)
        }
        canvas.restoreToCount(saveCount)
        canvas.translate(centerHorizontal, centerVertical)
        dialDrawable?.draw(canvas)

    }

    private fun refreshValues() {
        totalLeftPadding = (paddingLeft + extraPadding).toFloat()
        totalTopPadding = (paddingTop + extraPadding).toFloat()
        totalRightPadding = (paddingRight + extraPadding).toFloat()
        totalBottomPadding = (paddingBottom + extraPadding).toFloat()

        horizontalSize = paddingLeft + paddingRight + (extraPadding * 2) + dialDiameter.toFloat()
        verticalSize = paddingTop + paddingBottom + (extraPadding * 2) + dialDiameter.toFloat()

        tickPositionVertical = paddingTop + extraPadding / 2f
        centerHorizontal =
            totalLeftPadding + (horizontalSize - totalLeftPadding - totalRightPadding) / 2f
        centerVertical =
            totalTopPadding + (verticalSize - totalTopPadding - totalBottomPadding) / 2f
    }

    private fun toDp(value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
