package com.uiuia.helper

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {



    private var floatingButton: FloatingButton? = null
    private var dismissLayoutUnit: ((View?) -> Unit?)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        turnToAccessibilitySetting?.setOnClickListener {
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        startWechat?.setOnClickListener {
            CommonUtils.startAppByPackageName(this, "com.tencent.wework")
        }

        sendDataToService?.setOnClickListener {

            val tasks = getTestTasks()
            val tasksGson = Gson().toJson(tasks)

            sendBroadcast(Intent()?.apply {
                action = HelperService.HelperServiceReceiver
                putExtra("send_data_to_tasks", tasksGson)
            })
        }

        dialogPermission?.setOnClickListener {
            applicationContext?.let {
                if (floatingButton == null) {
                    floatingButton = FloatingButton(this)
                    floatingButton?.setOnClickListener {
                        startActivity(Intent(this@MainActivity, LayoutActivity::class.java))
                    }
                }
                floatingButton?.let { fb -> dismissLayoutUnit = ViewUtils.showFullFloatingWindow(it, fb) }
            }
        }

        closeDialogPermission?.setOnClickListener {
            dismissLayoutUnit?.let { it -> it(floatingButton) }
        }

    }


    private fun getTestTasks(): MutableList<AsTask> {
        val asTasks: MutableList<AsTask> = ArrayList()
        val task = AsTask()

//        var clickAgreement = AsWork()?.apply {
//            pageNodes = arrayListOf(
//                AsWork.Node()?.apply {
//                    id = "com.tencent.wework:id/cfs"
//                    text = "温馨提示"
//                },
//                AsWork.Node()?.apply {
//                    id = "com.tencent.wework:id/cfn"
//                    text = "取消"
//                }, AsWork.Node()?.apply {
//                    id = "com.tencent.wework:id/cfq"
//                    text = "同意"
//                })
//            assistAction = AsWork.AssistAction()?.apply {
//                targetNode = AsWork.Node()?.apply {
//                    id = "com.tencent.wework:id/cfq"
//                    text = "同意"
//                }
//                action = AssistActionEnum.CLICK
//            }
//            delay = 500
//            packageName = "com.tencent.wework"
//            workType = WorkEnum.ASSIST
//        }
//
//        var selectWorkType = AsWork()?.apply {
//            pageNodes = arrayListOf(
//                AsWork.Node()?.apply {
//                    id = "com.tencent.wework:id/kk6"
//                })
//            assistAction = AsWork.AssistAction()?.apply {
//                targetNode = AsWork.Node()?.apply {
//                   id = "com.tencent.wework:id/kk6"
//                }
//                action = AssistActionEnum.CLICK
//            }
//            delay = 6000
//            packageName = "com.tencent.wework"
//            workType = WorkEnum.ASSIST
//        }

        task.isEnable = true
        task.works = ArrayList()
//        task.works?.add(clickWechatLogin)
//        task.works?.add(clickAgreement)
//        task.works?.add(selectWorkType)
        asTasks.add(task)
        return asTasks
    }

}