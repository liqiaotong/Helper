package com.uiuia.helper

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.Exception

object HandlerUtils {

    fun loop(action: ()->Unit, delayed: Long? = 0, errorAction: (() -> Unit)? = null) {

        fun exaction(){
            try{
                action()
            }catch(e:Exception){
                try {
                    if (errorAction != null) errorAction()
                }catch (e:Exception){

                }
            }
        }

        Looper.myLooper()?.let {
            if (delayed == null || delayed <= 0) {
                Handler(it).post { exaction() }
            } else {
                Handler(it).postDelayed({ exaction() }, delayed)
            }
        } ?: run {
            exaction()
        }
    }

}