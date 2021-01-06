package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IBusCustomized {
    fun getCitys(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun getLines(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}