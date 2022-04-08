package com.uiuia.helper

import android.content.Context

object CommonUtils {

    fun startAppByPackageName(context: Context?, packageName: String) {
        try {
            packageName?.let {
                val launchIntent = context?.packageManager?.getLaunchIntentForPackage(it)
                launchIntent?.let { intent -> context?.startActivity(intent) }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}