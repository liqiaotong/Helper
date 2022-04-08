package com.uiuia.helper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.Settings
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {
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
                action = HelperService.HelperServiceClass
                putExtra("tasks", tasksGson)
            })
        }

    }

    private fun getTestTasks(): MutableList<AsTask> {
        val asTasks: MutableList<AsTask> = ArrayList()
        val task = AsTask()

        var clickGroup = AsWork()?.apply {
            pageNodes = arrayListOf(
                AsWork.Node()?.apply {
                    id = "com.tencent.wework:id/g3u"
                    text = "已阅读并同意 软件许可及服务协议 和 隐私政策"
                },
                AsWork.Node()?.apply {
                    id = "com.tencent.wework:id/a_c"
                    text = "微信登录"
                }, AsWork.Node()?.apply {
                    id = "com.tencent.wework:id/hmv"
                    text = "手机号登录"
                })
            assistAction = AsWork.AssistAction()?.apply {
                targetNode = AsWork.Node()?.apply {
                    id = "com.tencent.wework:id/a_c"
                    text = "微信登录"
                }
                action = AssistActionEnum.CLICK
            }
            delay = 1500
            packageName = "com.tencent.wework"
            workType = WorkEnum.ASSIST
        }

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