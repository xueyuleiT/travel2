package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IBusQuery {
    fun clearQueryHistory(jsonObject: JsonObject)
    fun listQueryHistory(jsonObject: JsonObject)
    fun saveQueryHistory(jsonObject: JsonObject)
    fun querySiteOrLine(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)

}