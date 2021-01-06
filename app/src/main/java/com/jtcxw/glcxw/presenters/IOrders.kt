package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IOrders {
    fun getMemberOrder(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun getOrderTypeList(jsonObject: JsonObject,smartRefreshLayout: SmartRefreshLayout)
}