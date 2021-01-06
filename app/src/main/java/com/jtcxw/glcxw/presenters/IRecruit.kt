package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IRecruit {
    fun getRecruitLine(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun joinRecruit(jsonObject: JsonObject)
    fun cancelRejoinRecruit(jsonObject: JsonObject)
}