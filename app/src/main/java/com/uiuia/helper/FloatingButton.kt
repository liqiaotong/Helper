package com.uiuia.helper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout

class FloatingButton : FrameLayout {

    private var view: View? = null
    private var listener: OnFloatingButtonListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context?) {
        view = context?.let { LayoutInflater.from(it).inflate(R.layout.layout_floating_button, this) }
    }

    override fun getLayoutParams(): ViewGroup.LayoutParams? {
        return view?.findViewById<View>(R.id.button)?.layoutParams
    }

    fun setFloatingButtonListener(listener: OnFloatingButtonListener?) {
        this.listener = listener
    }

}

interface OnFloatingButtonListener {
    fun onClick()
}