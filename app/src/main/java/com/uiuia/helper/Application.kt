package com.uiuia.helper

import android.app.Application

class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        LayoutCachedManager.startCachedLayouts(this)
    }

}