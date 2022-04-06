package com.uiuia.helper

import android.accessibilityservice.AccessibilityService
import android.content.*
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

import android.text.TextUtils
import android.view.accessibility.AccessibilityWindowInfo
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.DataOutputStream
import java.io.OutputStream


class HelperService : AccessibilityService() {

    private val gson: Gson = GsonBuilder().create()

    companion object {
        const val HelperServiceClass = "com.uiuia.helper.HelperService"
    }

    private var broadcast: BroadcastReceiver? = null
    private var accessibilityEvent: AccessibilityEvent? = null
    private val asTasks: MutableList<AsTask> = ArrayList()

    override fun onCreate() {
        super.onCreate()
        try {
            broadcast = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    try {
                        val data = intent?.getStringExtra("tasks")
                        if (!TextUtils.isEmpty(data)) {
                            val type = object : TypeToken<MutableList<AsTask>>() {}.type
                            var newTasks = gson.fromJson<MutableList<AsTask>>(data, type)
                            asTasks?.clear()
                            asTasks?.addAll(newTasks)
                        }
                    } catch (e: Exception) {

                    }
                }
            }
            registerReceiver(broadcast, IntentFilter(HelperServiceClass))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
        broadcast?.let {
            unregisterReceiver(it)
        }
    }

    override fun onAccessibilityEvent(ae: AccessibilityEvent?) {
        Log.d("accessibility", "onAccessibilityEvent")
        accessibilityEvent = ae
        doTask(ae)
    }

    private fun doTask(ae: AccessibilityEvent?) {
        val firstUnSolveTask = asTasks.firstOrNull { !it.isSolved && it.isEnable }
        firstUnSolveTask?.let {
            val firstUnSolveWork =
                firstUnSolveTask?.works?.firstOrNull { !it.isSolved && !it.isLocked }
            firstUnSolveWork?.let {
                when (it.workType) {
                    WorkEnum.ASSIST -> {
                        assistWork(ae, it)
                    }
                    WorkEnum.OPERATION -> {
                        operationWork(it)
                    }
                }
            }
        }
    }

    private fun operationWork(work: AsWork?) {
        val action = work?.operationAction
        if (action != null) {
            when (action) {


            }
        }
    }

    private fun assistWork(ae: AccessibilityEvent?, work: AsWork?) {

        fun check(an: AccessibilityNodeInfo?, node: AsWork.Node?): Boolean {
            var targetId = true
            var targetText = true
            var targetType = true
            var isIdNull = false
            var isTextNull = false
            var isTypeNull = false
            if (!TextUtils.isEmpty(an?.viewIdResourceName) && !TextUtils.isEmpty(node?.id)) {
                targetId = TextUtils.equals(an?.viewIdResourceName, node?.id)
            } else {
                isIdNull = true
            }
            if (!TextUtils.isEmpty(an?.text) && !TextUtils.isEmpty(node?.text)) {
                targetText =
                    TextUtils.equals(an?.text, node?.text)
            } else {
                isTextNull = true
            }
            if (!TextUtils.isEmpty(an?.className) && !TextUtils.isEmpty(node?.type)) {
                targetType =
                    TextUtils.equals(an?.className, node?.type)
            } else {
                isTypeNull = true
            }
            return (targetId && targetText && targetType) && !(isIdNull && isTextNull && isTypeNull)
        }

        if (ae != null && work != null) {
            if (TextUtils.equals(
                    ae.packageName,
                    work.packageName
                ) && work?.assistAction != null
            ) {

                val nodes = getNodes(rootInActiveWindow)

                var isTargetPage = work?.pageNodes?.all { w ->
                    nodes?.any { i -> check(i, w) } == true
                } ?: false

                if (isTargetPage && !work.isLocked) {

                    work?.isLocked = true

                    val targetNode = nodes?.firstOrNull {
                        check(it, work?.assistAction?.targetNode)
                    }
                    targetNode?.let {
                        doAction(work, it)
                        work?.isSolved = true
                    }
                }
            }
        }
    }

    private fun doAction(work: AsWork?, an: AccessibilityNodeInfo?) {

        val action = work?.assistAction
        val targetNode = action?.targetNode
        if (action != null && targetNode != null && an != null) {

            fun work() {
                when (action?.action) {
                    AssistActionEnum.CLICK -> {
                        var dowith = false

                        var finalNode: AccessibilityNodeInfo? = null

                        if (!TextUtils.isEmpty(targetNode?.id)) {
                            finalNode = an?.findAccessibilityNodeInfosByViewId(targetNode?.id ?: "")
                                ?.firstOrNull()
                        }

                        if ((finalNode == null) && !TextUtils.isEmpty(targetNode?.text)) {
                            an?.findAccessibilityNodeInfosByText(targetNode?.text ?: "")
                                ?.firstOrNull()
                                ?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                        }

                        finalNode?.let {
                            var rootNode: AccessibilityNodeInfo? = it
                            for (level in 0 until (targetNode?.parentLevel)) {
                                rootNode = it.parent
                            }
                            rootNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)

                            dowith = true
                        }

                        work?.isSolved = dowith
                    }
                    AssistActionEnum.MOVE -> {
                        work?.isSolved = true
                    }
                    AssistActionEnum.INPUT -> {
                        work?.isSolved = true
                    }
                    else -> {

                    }
                }
                work?.isLocked = false

                setSolved()

                HandlerUtils.loop({
                    doTask(accessibilityEvent)
                }, 1500)

            }

            if ((work?.delay ?: 0) > 0) {
                HandlerUtils.loop({
                    work()
                }, work?.delay ?: 0, {
                    work()
                })
            } else {
                work()
            }

        }
    }

    private fun setSolved() {
        asTasks?.forEach {
            val allWorkCompleted = it.works?.all { work -> work.isSolved } ?: true
            it.isSolved = allWorkCompleted
        }
    }

    private fun getNodes(
        node: AccessibilityNodeInfo?,
        level: Int? = null,
        currentLevel: Int? = null, log: Boolean? = false
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
                    if (log == true) {
                        Log.d(
                            "cavs",
                            "child id:${child?.viewIdResourceName ?: "null"}  text:${child?.text ?: "null"}"
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("cavs", "getNodes e:${e.printStackTrace()}")
                }
                getNodes(
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