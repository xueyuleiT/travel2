package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IService {
    fun getMisceAneous(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun getCusServerInfo(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}