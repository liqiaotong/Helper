package com.uiuia.helper

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.max

object ViewUtils {

    fun showFullFloatingWindow(context: Context, view: View, width: Int? = null, height: Int? = null): (View?) -> Unit? {

        var windowManager: WindowManager? = null

        var isCanDrawOverlays = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            true
        }
        if (isCanDrawOverlays) {
            windowManager = context.getSystemService(AppCompatActivity.WINDOW_SERVICE) as WindowManager
            if(view.windowToken!=null){
                windowManager?.removeView(view)
            }
            val layoutParams = WindowManager.LayoutParams()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
            }
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            layoutParams.format = PixelFormat.RGBA_8888
            layoutParams.width = width ?: view.layoutParams.width
            layoutParams.height = height ?: view.layoutParams.height
            windowManager?.addView(view, layoutParams)

            view?.post {

                view?.setOnTouchListener(object : View.OnTouchListener {

                    var isMoved = false
                    var downX = 0f
                    var downY = 0f
                    var lpX: Int = 0
                    var lpY: Int = 0
                    var displayRect: Rect? = getDisplay(context)
                    val maxNlpMoveX = abs((displayRect?.right ?: 0) * 0.5f - (view?.measuredWidth ?: 0) * 0.5f)
                    val maxNlpMoveY = abs((displayRect?.bottom ?: 0 - 1000) * 0.5f - (view?.measuredHeight ?: 0) * 0.5f)

                    override fun onTouch(view: View?, event: MotionEvent?): Boolean {

                        var x: Float = event?.rawX ?: 0f
                        var y: Float = event?.rawY ?: 0f

                        when (event?.action) {
                            MotionEvent.ACTION_DOWN -> {
                                downX = x
                                downY = y
                                lpX = layoutParams.x
                                lpY = layoutParams.y
                                isMoved = false
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val offsetX = x - downX
                                val offsetY = y - downY
                                val nlpX = (lpX + offsetX).toInt()
                                val nlpY = (lpY + offsetY).toInt()

                                if (abs(nlpX) <= maxNlpMoveX) {
                                    layoutParams.x = nlpX
                                } else {
                                    downX = x
                                    lpX = (if (nlpX >= 0) maxNlpMoveX else -maxNlpMoveX).toInt()
                                    layoutParams.x = lpX
                                }
                                if (abs(nlpY) <= maxNlpMoveY) {
                                    layoutParams.y = nlpY
                                } else {
                                    downY = y
                                    lpY = (if (nlpY >= 0) maxNlpMoveY else -maxNlpMoveY).toInt()
                                    layoutParams.y = lpY
                                }
                                windowManager?.updateViewLayout(view, layoutParams)
                                isMoved = true
                            }
                            MotionEvent.ACTION_UP -> {

                            }
                        }
                        return isMoved
                    }

                })
            }

        } else {

        }

        val dismiss = { vs:View? ->
            try {
                var vs = vs
                if(vs == null) vs = view
                if (vs?.windowToken != null) {
                    windowManager?.let { it.removeView(vs) }
                }
            } catch (e: Exception) {

            }
        }

        return dismiss
    }


    fun getDisplay(context: Context): Rect? {
        val disPlayMetrics = context.resources.displayMetrics
        return Rect(0, 0, disPlayMetrics.widthPixels, disPlayMetrics.heightPixels)
    }

}