package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IHome:IHotel,IScenic {
    fun getContentTypeList(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun getMemberInfo(jsonObject: JsonObject)
    fun h5ModuleConfig(jsonObject: JsonObject)

}