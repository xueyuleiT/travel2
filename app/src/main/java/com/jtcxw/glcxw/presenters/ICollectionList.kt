package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface ICollectionList {
    fun querySite(jsonObject: JsonObject,stationId:String)
    fun getCollectionInfo(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}