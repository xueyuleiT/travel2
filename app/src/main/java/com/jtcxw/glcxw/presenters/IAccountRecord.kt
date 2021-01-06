package com.jtcxw.glcxw.presenters

import com.google.gson.JsonObject
import com.scwang.smartrefresh.layout.SmartRefreshLayout

interface IAccountRecord {
    fun getMemberAccountHistory(jsonObject: JsonObject, smartRefreshLayout: SmartRefreshLayout)
}