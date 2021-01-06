package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IHotel {
    fun getHotelInfoList(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout)

}