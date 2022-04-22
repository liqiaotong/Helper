package com.uiuia.helper

import android.graphics.Rect
import android.text.TextUtils

class LayoutEntity {

    var id: String? = null
    var level: Int = 0
    var rect: Rect? = null
    var color: Int? = null
    var content:String? = null
    var type:String? = null

    fun getAllString():String{
        val string = StringBuffer()
        fun add(s:String?){
            if(!TextUtils.isEmpty(string)) string.append("/n")
            string.append(s)
        }
        add("id:$id")
        add("content:$content")
        add("type:$type")
        return string.toString()
    }

    fun getFocusString():String{
        if(!TextUtils.isEmpty(id)) return "id:$id"
        if(!TextUtils.isEmpty(content)) return "content:$content"
        if(!TextUtils.isEmpty(type)) return "type:$type"
        return ""
    }

}