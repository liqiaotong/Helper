package com.uiuia.helper

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

import android.text.TextUtils
import java.io.DataOutputStream
import java.io.OutputStream


class AccessibilityService : AccessibilityService() {

    private val tasks: MutableList<AnTask> = ArrayList()

    override fun onAccessibilityEvent(ae: AccessibilityEvent?) {
        Log.d("accessibility", "onAccessibilityEvent")
        //val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val firstUnSolveTask = tasks.firstOrNull{ !it.isSolved }
        val firstUnSolveWork = firstUnSolveTask?.works?.firstOrNull { !it.isSolved }

        ae?.let { ae ->
            if (TextUtils.equals(ae.packageName, "com.tencent.wework")) {
                val nodes = getChildNode(rootInActiveWindow)?.forEach {
                    Log.d("cavs", "node:${it?.text}")
//                    if(it?.text?.equals("消息")==true){
//                        Log.d("accessibility","equals 消息:${it?.text}")
//                        it?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                        rootInActiveWindow?.findAccessibilityNodeInfosByViewId(it.viewIdResourceName)?.firstOrNull()?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    }
                }
            }
        }
    }

    private fun doTask() {

    }

    private fun checkPage() {

    }

    private fun getChildNode(
        node: AccessibilityNodeInfo?,
        level: Int? = null,
        currentLevel: Int? = null
    ): MutableList<AccessibilityNodeInfo?>? {
        if (level != null && (currentLevel ?: 0) >= level) return null
        val nodes: MutableList<AccessibilityNodeInfo?> = ArrayList()
        node?.let {
            var size = if (it.childCount <= 0) 0 else (it.childCount - 1)
            for (index in 0..size) {
                val child: AccessibilityNodeInfo? = it.getChild(index)
                try {
                    if ((child?.viewIdResourceName != null) || (child?.text != null) || (child?.className != null)) {
                        nodes.add(child)
                    }
                    if (TextUtils.equals(child?.text?.toString(),"微餐时代")) {
                        Log.d("cavs", "广东爱瓦力科技 click")
                        var parentNode:AccessibilityNodeInfo? = child
                        for (index in 0..5){
                            parentNode = parentNode?.parent
                            parentNode?.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                            parentNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        }

                    }
                    //Log.d("cavs", "child id:${child?.viewIdResourceName ?: "null"}  text:${child?.text ?: "null"}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                getChildNode(
                    it.getChild(index),
                    level,
                    (currentLevel ?: 0) + 1
                )?.let { its -> nodes.addAll(its) }
            }
        }
        return nodes
    }


    override fun onInterrupt() {
        Log.d("accessibility", "onInterrupt")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("accessibility", "onServiceConnected")
    }


    fun adbShell(cmd: String) {
        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            val process = Runtime.getRuntime().exec("su")
            // 获取输出流
            val outputStream: OutputStream = process.outputStream
            val dataOutputStream = DataOutputStream(
                outputStream
            )
            dataOutputStream.writeBytes(cmd)
            dataOutputStream.flush()
            dataOutputStream.close()
            outputStream.close()
            Log.d("accessibility", "adbShell: 输出命令:" + cmd + "成功")
        } catch (t: Throwable) {
            t.printStackTrace()
            Log.d("accessibility", "adbShell: 输出命令:" + cmd + "失败")
        }
    }

}