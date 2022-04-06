package com.uiuia.helper

import android.content.Context

object CommonUtils {

    fun startAppByPackageName(context: Context, packageName: String) {
        packageName?.let {
            val launchIntent = context.packageManager.getLaunchIntentForPackage(it)
            context.startActivity(launchIntent)
        }
    }

}