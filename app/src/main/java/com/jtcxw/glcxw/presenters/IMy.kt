package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IMy {
    fun getMemberInfo(jsonObject: JsonObject)
    fun getOrderStatistics(jsonObject: JsonObject)
}