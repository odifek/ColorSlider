package com.example.colorslider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat

class ColorSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.seekBarStyle
) :
    AppCompatSeekBar(context, attrs, defStyleAttr) {

    private var colors: ArrayList<Int> = arrayListOf(Color.RED, Color.YELLOW, Color.BLUE)

    private val w = 48
    private val h = 48
    private val halfW = if (w >= 0) w / 2f else 1f
    private val halfH = if (h >= 0) h / 2f else 1f

    private val paint = Paint()

    private var noColorDrawable: Drawable? = null
        set(value) {
            w2 = value?.intrinsicWidth ?: 0
            h2 = value?.intrinsicHeight ?: 0
            halfW2 = if (w2 >= 0) w2 / 2 else 1
            halfH2 = if (h2 >= 0) h2 / 2 else 1
            field = value
        }
    private var w2 = 0
    private var h2 = 0
    private var halfW2 = 1
    private var halfH2 = 1

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorSlider)
        try {
            colors = typedArray.getTextArray(R.styleable.ColorSlider_colors)
                .map { Color.parseColor(it.toString()) }
                    as ArrayList<Int>
        } finally {
            typedArray.recycle()
        }

        colors.add(0, android.R.color.transparent)
        max = colors.size - 1
        progressBackgroundTintList =
            ContextCompat.getColorStateList(context, android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
        splitTrack = false
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom + 80)
        thumb = context.getDrawable(R.drawable.ic_arrow_drop_down)

        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach { it(colors[progress]) }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        noColorDrawable = context.getDrawable(R.drawable.ic_no_color)
    }

    var selectedColorValue: Int = android.R.color.transparent
        set(value) {
            val index = colors.indexOf(value)
            progress = if (index == -1) {
                0
            } else {
                index
            }
        }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTickMarks(canvas)
    }

    private fun drawTickMarks(canvas: Canvas?) {
        canvas?.let { canvas ->
            val count = colors.size
            val saveCount = canvas.save()
            canvas.translate(paddingLeft.toFloat(), (height / 2).toFloat() + 50f)
            if (count > 1) {
                for (i in 0 until count) {
                    val spacing = (width - paddingLeft - paddingRight) / (count - 1).toFloat()

                    if (i == 0) {
                        noColorDrawable?.setBounds(-halfW2, -halfH2, halfW2, halfH2)
                        noColorDrawable?.draw(canvas)
                    } else {
                        paint.color = colors[i]
                        canvas.drawRect(-halfW, -halfH, halfW, halfH, paint)
                    }
                    canvas.translate(spacing, 0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    private var listeners: ArrayList<(Int) -> Unit> = arrayListOf()
    fun addListener(function: (Int) -> Unit) {
        listeners.add(function)
    }
}