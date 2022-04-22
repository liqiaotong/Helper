package com.uiuia.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LayoutCachedManager {

    private val layoutsMap: HashMap<String, MutableList<LayoutEntity>?> = HashMap()
    private var lastCachedPackageName: String? = ""
    private var broadcast: BroadcastReceiver? = null
    private var callback: LayoutCachedCallback? = null

    fun setLayoutCachedCallback(callback: LayoutCachedCallback?) {
        this.callback = callback
    }

    fun getLayouts(packageName: String? = null): MutableList<LayoutEntity>? {
        return layoutsMap[packageName ?: lastCachedPackageName]
    }

    private fun saveLayouts(packageName: String?, data: String?) {
        Log.d("vvsa", "layoutview updateLayout datas packagename:$packageName")
        if (TextUtils.equals(packageName, "com.uiuia.helper")) return
        if (!TextUtils.isEmpty(data)) {
            try {
                val type = object : TypeToken<MutableList<LayoutEntity>?>() {}.type
                val layouts = Gson().fromJson<MutableList<LayoutEntity>?>(data, type)
                lastCachedPackageName = packageName
                layouts?.let { layoutsMap?.put(packageName ?: "", it) }
                callback?.let { it.update(packageName, layouts) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startCachedLayouts(context: Context?) {
        try {
            broadcast?.let { context?.unregisterReceiver(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            broadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        val packageName = intent?.getStringExtra("get_node_info_package")
                        val layouts = intent?.getStringExtra("get_node_info")
                        saveLayouts(packageName, layouts)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            context?.registerReceiver(broadcast, IntentFilter(HelperService.HelperServiceSend))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

interface LayoutCachedCallback {
    fun update(packageName: String?, layouts: MutableList<LayoutEntity>?)
}