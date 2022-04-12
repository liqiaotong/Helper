package com.uiuia.helper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_layout.*


class LayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        LayoutCachedManager?.getLayouts().let {
            Log.d("vvsa", "layoutview update layouts by create")
            layoutView?.updateLayout(it)
        }

        LayoutCachedManager?.setLayoutCachedCallback(object :LayoutCachedCallback{
            override fun update(packageName: String?, layouts: MutableList<LayoutEntity>?) {
                Log.d("vvsa", "layoutview update layouts by callback")
                layoutView?.updateLayout(layouts)
            }
        })
    }

}