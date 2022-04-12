package com.uiuia.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class LayoutView : View {

    private var paint: Paint? = null
    private var layouts: MutableList<LayoutEntity> = ArrayList()
    private var color: Int = Color.parseColor("#32DC143C")

    fun updateLayout(layouts: MutableList<LayoutEntity>?) {
        this.layouts.clear()
        layouts?.let { this.layouts.addAll(it) }
        invalidate()
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initResource(context, attrs, defStyleAttr)
    }

    private fun initResource(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
//        context?.let {
//            var a = context.obtainStyledAttributes(attrs, R.styleable.AnColorView, defStyleAttr, 0)
//            mStartColor = a.getColor(R.styleable.AnColorView_acv_startColor, defaultStartColor)
//            a.recycle()
//        }
        invalidate()
    }

    init {
        initPaint()
    }

    private fun initPaint() {
        paint = Paint()
        paint?.isAntiAlias = true
        paint?.style = Paint.Style.FILL
        paint?.color = color
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //paint?.let { canvas?.drawRect(100f,100f, 300f, 300f, it) }

        let bc@{
            var count = 0
            paint?.let { paint ->
                layouts?.forEach {
                    val rect = it?.rect
                    rect?.let { r ->
                        canvas?.drawRect(r.left.toFloat(), r.top.toFloat(), r.right.toFloat(), r.bottom.toFloat(), paint)
                    }
                    if (count >= 5) return@bc
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

}