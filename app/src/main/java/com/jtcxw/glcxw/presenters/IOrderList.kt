package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IOrderList {
    fun getListByCustomer(params: JsonObject,smartRefreshLayout: SmartRefreshLayout)
    fun deleteBusOrder(params: JsonObject)
    fun pay(params: JsonObject)


}