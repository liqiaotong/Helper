package com.uiuia.helper

import android.content.Context
import android.os.Build
import android.view.WindowManager
import androidx.annotation.RequiresApi


object CommonUtils {

    fun startAppByPackageName(context: Context?, packageName: String) {
        try {
            packageName?.let {
                val launchIntent = context?.packageManager?.getLaunchIntentForPackage(it)
                launchIntent?.let { intent -> context?.startActivity(intent) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}