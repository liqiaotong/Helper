package com.uiuia.helper

import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class LayoutView : View {

    //private var windowSize:RectF? = RectF()
    private var paint: Paint? = null
    private var idPaint: Paint? = null
    private var layouts: MutableList<LayoutEntity> = ArrayList()
    private var color: Int = Color.parseColor("#32DC143C")
    private var idColor: Int = Color.parseColor("#FFFFFF")
    private val idRect = Rect()

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

        idPaint = Paint()
        idPaint?.isAntiAlias = true
        idPaint?.style = Paint.Style.FILL
        idPaint?.color = idColor
        idPaint?.textAlign = Paint.Align.CENTER
    }

//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//        windowSize = RectF(0f,0f,w.toFloat(),h.toFloat())
//    }

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
                        val content = it.getFocusString()
                        if (!TextUtils.isEmpty(content)) {
                            val pointCenterY = (r.bottom+r.top)*0.5f
                            val pointCenterX = (r.right+r.left)*0.5f
                            idPaint?.let { np ->
                                np?.getTextBounds(content, 0, content!!.length, idRect)
                                val textCenterY = (pointCenterY) + idRect.height() * 0.5f
                                canvas?.drawText(content!!, pointCenterX, textCenterY, np)
                            }
                        }
                    }
                    if (count >= 10) return@bc
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

}