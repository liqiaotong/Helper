package com.uiuia.helper

import android.content.Context
import android.graphics.Canvas
import android.view.View

class LayoutView : View {

    private var layouts: MutableList<LayoutEntity> = ArrayList()

    fun updateLayout(layouts: MutableList<LayoutEntity>?) {
        this.layouts.clear()
        layouts?.let { this.layouts.addAll(it) }
        invalidate()
    }

    constructor(context: Context) : super(context) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        layouts?.forEach {
            //canvas.dra
        }
    }

}