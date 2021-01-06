package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject

interface IGoodsInfo {
    fun getAccountInfo(jsonObject: JsonObject)
}